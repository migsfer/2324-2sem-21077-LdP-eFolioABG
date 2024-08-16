(**========================================================================
Name        : efolioA
Author      : 
Description : 
Date        : 06.05.2024

NOTAS       :


efolioA-LdP
============================================================================**)
open Str

let read_file filename =
  let lines = ref [] in
  let channel = open_in filename in
  try
    while true do
      lines := input_line channel :: !lines
    done; []
  with End_of_file ->
    close_in channel;
    List.rev !lines

let parse_discount line =
  let regexp = Str.regexp "discount('\\(.*\\)', \\(.*\\))." in
  if Str.string_match regexp line 0 then
    Some (Str.matched_group 1 line, float_of_string (Str.matched_group 2 line))
  else
    None

let parse_item line =
  let regexp = Str.regexp "item(\\(.*\\), '\\(.*\\)', '\\(.*\\)', \\(.*\\), \\(.*\\))." in
  if Str.string_match regexp line 0 then
    Some (int_of_string (Str.matched_group 1 line), Str.matched_group 2 line, Str.matched_group 3 line, float_of_string (Str.matched_group 4 line), int_of_string (Str.matched_group 5 line))
  else
    None

let parse_loyalty_discount line =
  let regexp = Str.regexp "loyalty_discount(\\(.*\\), \\(.*\\))." in
  if Str.string_match regexp line 0 then
    Some (Str.matched_group 1 line, float_of_string (Str.matched_group 2 line))
  else
    None

let parse_shipping_cost line =
  let regexp = Str.regexp "shipping_cost('\\(.*\\)', \\(.*\\))." in
  if Str.string_match regexp line 0 then
    Some (Str.matched_group 1 line, float_of_string (Str.matched_group 2 line))
  else
    None


let checkout_Wo_dscnts cart_items =
  let parse_cart_item cart_str =
    match String.split_on_char ',' cart_str with
    | [id_str; _; _; price_str; quantity_str] ->
        let price = float_of_string price_str in
        let quantity = int_of_string quantity_str in
        price *. float_of_int quantity
    | _ -> failwith "Invalid cart item format"
  in
  let items_subtotals = List.map parse_cart_item (String.split_on_char ';' cart_items) in
  let total_price = List.fold_left (+.) 0.0 items_subtotals in
  total_price

let checkout_Ctgry_dscnts discounts cart_items =
  let parse_cart_item cart_str =
    match String.split_on_char ',' cart_str with
    | [id_str; _; category; price_str; quantity_str] ->
        let price = float_of_string price_str in
        let quantity = int_of_string quantity_str in
        (category, price *. float_of_int quantity) (* Return category and subtotal *)
    | _ -> failwith "Invalid cart item format"
  in
  let apply_discount price category =
    match List.assoc_opt category discounts with
    | Some discount ->
        let discounted_price = price *. (1.0 -. discount) in
        (price -. discounted_price) (* Return the applied discount *)
    | None -> 0.0 (* No discount applied *)
  in
  let items = String.split_on_char ';' cart_items in
  let total_discount =
    List.fold_left (fun acc item ->
      let category, subtotal = parse_cart_item item in
      let discount = apply_discount subtotal category in
      acc +. discount) (* Sum up the discounts *)
      0.0 items
  in
  (**Printf.printf "Total value of discounts applied: %.2f\n" total_discount;**)
  flush stdout;
  total_discount

let checkout_Loyalty_dscnt loyalty_discounts lyltyY_and_CatDscnt =
  let parse_lyltyY_and_CatDscnt str =
    match String.split_on_char ';' str with
    | [loyaltyYearsStr; checkoutCtgryDscntStr] ->
        (loyaltyYearsStr, checkoutCtgryDscntStr)
    | _ -> failwith "Invalid loyalty years and category discount format"
  in
  let loyaltyYearsStr, checkoutCtgryDscntStr = parse_lyltyY_and_CatDscnt lyltyY_and_CatDscnt in
  let loyaltyYears =
    try int_of_string loyaltyYearsStr
    with Failure _ -> failwith "Invalid loyalty years format"
  in
  let checkoutCtgryDscnt =
    try float_of_string checkoutCtgryDscntStr
    with Failure _ -> failwith "Invalid category discount format"
  in
  let rec find_discount loyaltyYears = function
    | [] -> 0.0 (* If no discount found, return 0.0 *)
    | (years, discount) :: rest ->
        if years = string_of_int loyaltyYears || (years = ">5" && loyaltyYears > 5) then
          discount
        else
          find_discount loyaltyYears rest
  in
  let loyaltyDiscount = find_discount loyaltyYears loyalty_discounts in
  (**let checkoutPriceWithLoyaltyDscnt = checkoutCtgryDscnt *. (1.0 -. loyaltyDiscount) in**)
  let checkoutPriceWithLoyaltyDscnt = checkoutCtgryDscnt *. (loyaltyDiscount) in
  (**Printf.printf "Checkout Price with loyalty discounts: %f\n" checkoutPriceWithLoyaltyDscnt;**)
  flush stdout;
  checkoutPriceWithLoyaltyDscnt

let provide_shipcost_price shipping_costs district =
  match List.assoc_opt district shipping_costs with
  (**| Some cost -> string_of_float cost**)
  | Some cost -> cost
  (**| None -> failwith "Shipping cost for the given district not found"**)
  | None -> 250.0(**We got you, it just costs more**)

let provide_checkout_price_with_shipcost cartclient discounts loyalty_discounts shipping_costs =
  match String.split_on_char '-' cartclient with
  | [cart_items; loyalty_years; district] ->
    let total_wo_discount = checkout_Wo_dscnts cart_items in
    let total_after_ctgry_dscnts = total_wo_discount -. (checkout_Ctgry_dscnts discounts cart_items) in
    let total_after_loyalty_dscnt = total_after_ctgry_dscnts -. (checkout_Loyalty_dscnt loyalty_discounts (loyalty_years ^ ";" ^ (string_of_float total_after_ctgry_dscnts))) in
    let total_with_shipping = total_after_loyalty_dscnt +. (provide_shipcost_price shipping_costs district) in
    (**Printf.printf "Total price with shipping cost: %.2f\n" total_with_shipping;**)
    total_with_shipping
  | _ -> failwith "Invalid input format"

let provide_sorted_cart cart_items =
  let parse_cart_item cart_str =
    match String.split_on_char ',' cart_str with
    | [id_str; name; category; price_str; quantity_str] ->
        let id = int_of_string id_str in
        let price = float_of_string price_str in
        let quantity = int_of_string quantity_str in
        (id, name, category, price, quantity)
    | _ -> failwith "Invalid cart item format"
  in
  let compare_cart_items (_, name1, category1, _, _) (_, name2, category2, _, _) =
    match String.compare category1 category2 with
    | 0 -> String.compare name1 name2
    | cmp -> cmp
  in
  let cart_list = String.split_on_char ';' cart_items in
  let parsed_cart = List.map parse_cart_item cart_list in
  let sorted_cart = List.sort compare_cart_items parsed_cart in
  let sorted_cart_str = String.concat ";" (List.map (fun (id, name, category, price, quantity) ->
    Printf.sprintf "%d,%s,%s,%.2f,%d" id name category price quantity) sorted_cart)
  in
  sorted_cart_str


let main () =
    let file_lines = read_file "store.pl" in
    let discounts = List.filter_map parse_discount file_lines in
    let items = List.filter_map parse_item file_lines in
    let loyalty_discounts = List.filter_map parse_loyalty_discount file_lines in
    let shipping_costs = List.filter_map parse_shipping_cost file_lines in

  match Sys.argv with
  | [| _; "checkout_Wo_dscnts"; cart_items |] ->
    let total_wo_discount = checkout_Wo_dscnts cart_items in
    Printf.printf "Total price without discounts:%s\n" (string_of_float total_wo_discount)

  | [| _; "checkout_Ctgry_dscnts"; cart_items |] ->
    let total_ctgry_dscnts = checkout_Ctgry_dscnts discounts cart_items in
    Printf.printf "Total price with category discounts:%s\n" (string_of_float total_ctgry_dscnts)

  | [| _; "checkout_Loyalty_dscnt"; lyltyY_and_CatDscnt |] ->
    let total_w_LoyaltyDscnt = checkout_Loyalty_dscnt loyalty_discounts lyltyY_and_CatDscnt in
    Printf.printf "Total price with Loyalty discount:%s\n" (string_of_float total_w_LoyaltyDscnt)

  | [| _; "shipcost_by_distrito"; district |] ->
    let total_shipcost = provide_shipcost_price shipping_costs district in
    Printf.printf "Total price OF shipping cost:%s\n" (string_of_float total_shipcost)

  | [| _; "checkout_with_shipcost"; cart_client |] ->
    let total_w_shipcost = provide_checkout_price_with_shipcost cart_client discounts loyalty_discounts shipping_costs in
    Printf.printf "Total price with shipping cost:%s\n" (string_of_float total_w_shipcost)

  | [| _; "sort_cart"; cart_items |] ->
    let sorted_cart = provide_sorted_cart cart_items in
    Printf.printf "%s\n" sorted_cart

  | _ ->
    (* Print the parsed information *)
    List.iter (fun (id, name, category, price, quantity) -> Printf.printf "Item: %d, %s, %s, %f, %d\n" id name category price quantity) items;
    List.iter (fun (category, discount) -> Printf.printf "Discount for category %s: %f\n" category discount) discounts;
    List.iter (fun (years, discount) -> Printf.printf "Loyalty discount for %s year(s): %f\n" years discount) loyalty_discounts;
    List.iter (fun (district, cost) -> Printf.printf "Shipping cost to district %s: %f\n" district cost) shipping_costs

let () = main ()
