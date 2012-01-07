Bumeráng Letöltő
================

Hey guys! This project is related to a hungarian radio show, so there is no point using english here.

Üdv minden érdeklődőnek! Ez a kis alkalmazás arra hivatott, hogy a segítségével letölthessük a Bumeráng .c műsor adásait. Szerencsére a show adásainak teljes anyaga elérhető a www.bumerang.hu oldalon, azonban egy-egy adás 10-12 darabra szét van tördelve, így hosszú távon elég kínos egyesével letöltögetni az adásokat. Ebben segít az alkalmazás: Letölti a kért adásokat, akár egy teljes hónapnyi adagot is egyben, adásonként mappába rakja őket (dátum szerint), az egyes mp3-aknak értelmes nevet ad (szám, cím).

Használat
---------

Az alkalmazás parancssoros, használata viszonylag egyszerű. Ha mindenféle paraméterezés nélkül indítod el, akkor egy súgót láthatsz a használatról, úgyhogy itt nagyvonalakban csupán:


Részletes súgót kapsz, ha elindítod az alkalmazást paraméterek nélkül. Alapjában véve így fogod használni:
  java -jar bumerang.jar --start 2011.10.14
vagy
  java -jar bumerang.jar --start 2011.08.01 --end 2011.08.31

A --start és --end paraméterrel határozható meg, hogy mit szeretnél pontosan letölteni. Ha csak a --start van meghatározva, akkor az aznapit, ha az --end is meg van adva, akkor a két dátum közti összes adást le fogja tölteni a program.

Megjegyzések
------------

Az alkalmazás JAVA nyelven íródott. Egyszerűbb lett volna valami scriptnyelven elkészíteni, ahogy azt tettem a korábbi python alapú változattal, többek közt azért esett a JAVAra a választásom, mert most ezt tanulom.

Mint azt írtam, az alkalmazás parancsosos. Lehet ennek nem örülni, de számomra ez az egyszerűbb mind kivitelezésben, mind használatban. Persze, ha van rá igény szívesen csinálok hozzá grafikus felületet, és/vagy bővítem a program képességeit, de amíg csak én használom, ez így megfelelő.

A program úgy vélem működik. Nagy hibák valószínűleg nincsenek benne. Ha mégis találkoztok valami problémával, jelezzétek.


