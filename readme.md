# Endpoints para la carga del menú

Tipos de platos del menú: [http://www.mocky.io/v2/59494945120000b907272838](http://www.mocky.io/v2/59494945120000b907272838).

Platos del menú: [http://www.mocky.io/v2/594a303610000061011aa37d](http://www.mocky.io/v2/594a303610000061011aa37d).

Alergenos: [http://www.mocky.io/v2/594949ee120000ba0727283b](http://www.mocky.io/v2/594949ee120000ba0727283b).

Repositorio de imágenes: [http://comandas.digestivethinking.com/images/(resolución)/(archivo)](http://comandas.digestivethinking.com/images/xxxhdpi/img_carta_postres.png).

# Funcionamiento

Al lanzar la aplicación baja la estructura de la carta de los tres endpoints de arriba. Primero los tipos de plato, luego los alérgenos y por último todos los platos de la carta.

Esto lo hace una única vez. Ya que aún no manejo SQLite en android, voy a persistir los datos en fichero. Problema, la carta no volverá a cambiar jamás a no ser que cambiemos los datos de los endpoints y reinstalemos la aplicación.
