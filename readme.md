// ========================================================================  
// Name        : efolioA  
// Date        : 06.05.2024  
//   
// NOTAS       :  
//   
//   
// efolioA-LdP  
// ========================================================================  

Escrevo aqui as notas para compilar o código entregue para este efolioA
o meu ambiente é GNU/Linux Debian12

1-compilar ficheiro ocaml main.ml
$ ocamlc -o lojaMago str.cma main.ml

1.1-testar ocaml normal sem integração java
$ ./lojaMago
1.2-testar total sem desconto
$ ./lojaMago  checkout_Wo_dscnts "1,Potion of Healing,potions,10.00,10;2,Wand of Fireball,wands,20.00,2;3,Enchanted Spellbook,enchanted_books,30.00,3;4,Crystal of Clairvoyance,crystals,15.00,4;5,Amulet of Protection,amulets,25.00,20;1,Potion of Healing,potions,10.00,5"
1.3-testar desconto category
$ ./lojaMago checkout_Ctgry_dscnts "1,Potion of Healing,potions,10.00,10;2,Wand of Fireball,wands,20.00,2;3,Enchanted Spellbook,enchanted_books,30.00,3;4,Crystal of Clairvoyance,crystals,15.00,4;5,Amulet of Protection,amulets,25.00,20;1,Potion of Healing,potions,10.00,5"
1.4-testar desconto loyalty
$ ./lojaMago checkout_Loyalty_dscnt "3;656"
1.5-testar shipcost
$ ./lojaMago shipcost_by_distrito "Aveiro"
1.6-testar total com descontos e shipcost
$ ./lojaMago checkout_with_shipcost "1,Potion of Healing,potions,10.00,10;2,Wand of Fireball,wands,20.00,2;3,Enchanted Spellbook,enchanted_books,30.00,3;4,Crystal of Clairvoyance,crystals,15.00,4;5,Amulet of Protection,amulets,25.00,20;1,Potion of Healing,potions,10.00,5-3-Aveiro"

2-compilar ficheiros java
$ javac Cart.java Store.java Client.java Item.java
or
$ javac *.java

3-testar efolioA
$ java Store

