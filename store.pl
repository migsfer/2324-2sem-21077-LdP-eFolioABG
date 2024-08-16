% store.pl
% Item in inventoty
:- dynamic item/5.

item(1, 'Potion of Healer', potions, 11.0, 45).
item(2, 'Wand of Fireball', wands, 20.0, 27).
item(3, 'Enchanted Spellbook ADVANCED', enchanted_books, 30.0, 18).
item(4, 'Crystal of Clairvoyance', crystals, 15.0, 36).
item(5, 'Amulet of Protection', amulets, 25.0, 22).
item(6, 'Standard Wand', wands, 20.0, 90).
item(7, 'Love Potion', potions, 10.0, 45).
item(8, 'Advanced Spellbook', enchanted_books, 15.0, 27).
item(9, 'Crystal of Magic Vision', crystals, 30.0, 18).
item(10, 'Flying Broomstick', accessories, 50.0, 9).
item(11, 'Enchanted Scroll', scrolls, 8.0, 45).
item(12, 'Fairy Dust', ingredients, 5.0, 90).
item(13, 'pedra dourada', crystals, 30.0, 180).
item(16, 'magia do grande', enchanted_books, 35.0, 27).
item(17, 'brick dust', ingredients, 2.0, 45).

% Discount per item category
:- dynamic discount/2.

discount(wands, 0.2).
discount(enchanted_books, 0.3).
discount(crystals, 0.15).
discount(amulets, 0.25).
discount(accessories, 0.0).
discount(ingredients, 0.05).
discount(potions, 0.03).
discount(scrolls, 0.27).

% Discount by loyalty per year
:- dynamic loyalty_discount/2.

loyalty_discount(1, 0.05).
loyalty_discount(2, 0.1).
loyalty_discount(3, 0.15).
loyalty_discount(4, 0.2).
loyalty_discount(5, 0.25).
loyalty_discount(9, 0.3).

% Shipping Costs per Zone
:- dynamic shipping_cost/2.

shipping_cost('Braga', 2.5).
shipping_cost('Viseu', 3.0).
shipping_cost('Coimbra', 5.0).
shipping_cost('Aveiro', 5.0).
shipping_cost('Lisboa', 7.0).
shipping_cost('Porto', 10.0).
shipping_cost('Faro', 15.0).

% clients DB
:- dynamic clients/4.

clients(1, 'Alice', 'Aveiro', 3).
clients(2, 'Beatriz', 'Braga', 2).
clients(3, 'Carlos', 'Coimbra', 2).
clients(4, 'Diogo', 'Lisboa', 4).
clients(5, 'Eva', 'Faro', 1).
clients(6, 'Francisca', 'Faro', 3).
clients(7, 'Guilhermina', 'Viseu', 5).
clients(8, 'Guilherme', 'Lisboa', 4).
clients(9, 'Manuel', 'Vila Real', 2).

% purchase_history by client
:- dynamic purch_hist/7.

purch_hist(1, '20/05/2024', 50, 5.0, 0.0, 5.0, 50.0).
purch_hist(2, '21/05/2024', 30, 3.0, 1.0, 3.0, 29.0).
purch_hist(3, '22/05/2024', 40, 4.0, 0.0, 4.0, 40.0).
purch_hist(4, '23/05/2024', 60, 6.0, 2.5, 6.0, 57.5).
purch_hist(5, '23/05/2024', 25, 2.5, 0.0, 2.5, 25.0).
purch_hist(6, '25/05/2024', 35, 3.5, 2.0, 3.5, 33.0).
purch_hist(7, '26/05/2024', 75, 7.5, 0.0, 7.5, 75.0).
purch_hist(3, '27/05/2024', 45, 4.5, 0.0, 4.5, 45.0).
purch_hist(4, '28/05/2024', 55, 5.5, 10.0, 5.0, 44.5).
purch_hist(1, '28/05/2024', 60, 6.0, 0.0, 6.0, 60.0).
purch_hist(8, '28/05/2024', 60, 6.0, 0.0, 6.0, 60.0).
purch_hist(2, '29/05/2024', 150, 0, 15, 2.5, 137.5).
purch_hist(9, '30/05/2024', 1520, 0, 152, 250.0, 1618).

