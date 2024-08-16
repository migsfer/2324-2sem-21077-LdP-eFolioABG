:- use_module(library(aggregate)).
:- consult('store.pl').

%2.2 predicado para guardar a BD completa
save_store_to_file(FileName) :-
    tell(FileName),
    write('% store.pl'), nl,
    write('% Item in inventoty'), nl,
    listing(item/5),
    write('% Discount per item category'), nl,
    listing(discount/2),
    write('% Discount by loyalty per year'), nl,
    listing(loyalty_discount/2),
    write('% Shipping Costs per Zone'), nl,
    listing(shipping_cost/2),
    write('% clients DB'), nl,
    listing(clients/4),
    write('% purchase_history by client'), nl,
    listing(purch_hist/7),
    told.

%2.2 predicado eliminar todos os factos item
retract_all_items :-
    retractall(item(_, _, _, _, _)).

%2.2 predicado adicionar factos item
assert_item(ItemId, ItemName, ItemCategory, ItemPrice, ItemQuantity) :-
    assertz(item(ItemId, ItemName, ItemCategory, ItemPrice, ItemQuantity)).

%2.2 predicado para 2 algarismos significativos
round_value(Value, Rounded) :-
    Rounded is round(Value * 100) / 100.

%2.2 predicado para adicionar factos purch_hist
add_purch_hist(ClientId, Date, ChkWoDscnt, ChkCtgryDscnts, ChkLyltyDscnt, ShipCost, ChkTotal) :-
    round_value(ChkCtgryDscnts, ChkCtgryDscntsOut),
    round_value(ChkLyltyDscnt, ChkLyltyDscntOut),
    round_value(ChkTotal, ChkTotalOut),
    assertz(purch_hist(ClientId, Date, ChkWoDscnt, ChkCtgryDscntsOut, ChkLyltyDscntOut, ShipCost, ChkTotalOut)).

%3.1 predicado ver hist por data
purch_hist_by_date(Date, Purchases) :-
    findall([Id, Date, Cost, CategoryDiscount, LoyalDiscount, ShipCost, Total],
            purch_hist(Id, Date, Cost, CategoryDiscount, LoyalDiscount, ShipCost, Total),
            Purchases).

%3.2+6.3 predicado ver hist por cliente
purch_hist_by_clt(CltId, Purchases) :-
    findall([CltId, Date, Cost, CategoryDiscount, LoyalDiscount, ShipCost, Total],
            purch_hist(CltId, Date, Cost, CategoryDiscount, LoyalDiscount, ShipCost, Total),
            Purchases).

%3.3 predicado ver hist por distrito
purch_hist_by_distrito(Distrito, Purchases) :-
    downcase_atom(Distrito, LowerDistrito),
    findall([CltId, Date, Cost, CategoryDiscount, LoyalDiscount, ShipCost, Total],
            ((clients(CltId, _, DistritoDB, _), downcase_atom(DistritoDB, LowerDistrito)),
             purch_hist(CltId, Date, Cost, CategoryDiscount, LoyalDiscount, ShipCost, Total)),
            Purchases).

%3.4 predicado ver totais hist por distrito
purch_hist_sum_distrito(Distrito, Totals) :-
    downcase_atom(Distrito, LowerDistrito),
    aggregate_all(sum(Cost), (clients(CltId, _, DistritoDB, _), downcase_atom(DistritoDB, LowerDistrito), purch_hist(CltId, _, Cost, _, _, _, _)), TotalCost),
    aggregate_all(sum(CategoryDiscount), (clients(CltId, _, DistritoDB, _), downcase_atom(DistritoDB, LowerDistrito), purch_hist(CltId, _, _, CategoryDiscount, _, _, _)), TotalCategoryDiscount),
    aggregate_all(sum(LoyalDiscount), (clients(CltId, _, DistritoDB, _), downcase_atom(DistritoDB, LowerDistrito), purch_hist(CltId, _, _, _, LoyalDiscount, _, _)), TotalLoyalDiscount),
    aggregate_all(sum(ShipCost), (clients(CltId, _, DistritoDB, _), downcase_atom(DistritoDB, LowerDistrito), purch_hist(CltId, _, _, _, _, ShipCost, _)), TotalShipCost),
    aggregate_all(sum(Total), (clients(CltId, _, DistritoDB, _), downcase_atom(DistritoDB, LowerDistrito), purch_hist(CltId, _, _, _, _, _, Total)), TotalTotal),
    Totals = [TotalCost, TotalCategoryDiscount, TotalLoyalDiscount, TotalShipCost, TotalTotal].


%3.5 predicado ver totais hist por data
purch_hist_sum_date(Date, Totals) :-
    aggregate_all(sum(Cost), purch_hist(_, Date, Cost, _, _, _, _), TotalCost),
    aggregate_all(sum(CategoryDiscount), purch_hist(_, Date, _, CategoryDiscount, _, _, _), TotalCategoryDiscount),
    aggregate_all(sum(LoyalDiscount), purch_hist(_, Date, _, _, LoyalDiscount, _, _), TotalLoyalDiscount),
    aggregate_all(sum(ShipCost), purch_hist(_, Date, _, _, _, ShipCost, _), TotalShipCost),
    aggregate_all(sum(Total), purch_hist(_, Date, _, _, _, _, Total), TotalTotal),
    Totals = [TotalCost, TotalCategoryDiscount, TotalLoyalDiscount, TotalShipCost, TotalTotal].

%3.6 auxiliar soma CategoryDiscount and LoyalDiscount por distrito
district_discount_sum(Distrito, Sum) :-
    clients(CltId, _, Distrito, _),
    findall(Discount, (
        purch_hist(CltId, _, _, CategoryDiscount, LoyalDiscount, _, _),
        Discount is CategoryDiscount + LoyalDiscount
    ), Discounts),
    sum_list(Discounts, Sum).

%3.6 predicado maior soma de CategoryDiscount + LoyalDiscount
district_with_highest_discount(Distrito) :-
    findall(Sum-Distrito, district_discount_sum(Distrito, Sum), CitySums),
    keysort(CitySums, SortedCitySums),
    reverse(SortedCitySums, [_-Distrito|_]).

%3.6 auxiliar soma lista
sum_list([], 0).
sum_list([H|T], Sum) :-
    sum_list(T, Rest),
    Sum is H + Rest.

%4.2 predicado ver item por cat
items_by_category(Category, Items) :-
    downcase_atom(Category, LowerCategory),
    findall([ItemId, Name, Category, Price, Stock],
            (item(ItemId, Name, CategoryDB, Price, Stock), downcase_atom(CategoryDB, LowerCategory)),
            Items).

%4.3 predicado ver cat
display_categories(Categories) :-
    findall(Category, discount(Category, _), CategoryList),
    sort(CategoryList, Categories).


%4.4 predicado adicionar factos discount
assert_discount(CatName, CatDscnt) :-
    round_value(CatDscnt, CatDscntOut),
    assertz(discount(CatName, CatDscntOut)).

%4.5 predicado para ordenar items por id
sortItemsById :-
    findall(item(ItemId, Name, Category, Price, Stock), item(ItemId, Name, Category, Price, Stock), Items),
    sort(1, @=<, Items, SortedItems),
    retract_all_items,
    assert_sorted_items(SortedItems).

%4.5 auxiliar para assert items ordenados
assert_sorted_items([]).
assert_sorted_items([item(ItemId, Name, Category, Price, Stock) | Rest]) :-
    assert_item(ItemId, Name, Category, Price, Stock),
    assert_sorted_items(Rest).


%6.2 predicado ver clientes por distrito
client_by_district(Distrito, Result) :-
    downcase_atom(Distrito, LowerDistrito),
    findall([ClientID, Name, Distrito, CustomerYears], 
            (clients(ClientID, Name, DistritoDB, CustomerYears), downcase_atom(DistritoDB, LowerDistrito)),
            Result).

%6.4 predicado ver cat
clients_with_more_years(Years, Result) :-
    findall([ClientID, Name, Distrito, CustomerYears], 
            (clients(ClientID, Name, Distrito, CustomerYears), CustomerYears >= Years),
            Result).

%6.5 predicado para ordenar clients por id
sortClientsById :-
    findall(clients(ClientId, Name, Distrito, CustomerYears), clients(ClientId, Name, Distrito, CustomerYears), Clients),
    sort(1, @=<, Clients, SortedClients),
    retract_all_clients,
    assert_sorted_clients(SortedClients).

%6.5 auxiliar 
retract_all_clients :-
    retractall(clients(_, _, _, _)).

%6.5 auxiliar
assert_client(ClientId, Name, Distrito, CustomerYears) :-
    assertz(clients(ClientId, Name, Distrito, CustomerYears)).

%6.5 auxiliar
assert_sorted_clients([]).
assert_sorted_clients([clients(ClientId, Name, Distrito, CustomerYears) | Rest]) :-
    assert_client(ClientId, Name, Distrito, CustomerYears),
    assert_sorted_clients(Rest).


% predicado actualizar factos item
% update_item(ItemId, ItemName, ItemCategory, ItemPrice, ItemQuantity) :-
%     retract(item(ItemId, _, _, _, _)), % elimina facto antigo
%     assertz(item(ItemId, ItemName, ItemCategory, ItemPrice, ItemQuantity)). % adiciona facto actual
