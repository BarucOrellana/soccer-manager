## Sobre Soccer Manager

Soccer Manager es una aplicación de escritorio que está enfocada en ayudar a las pequeñas canchas de futbol amateur con la administración de los juegos, incluye características como conteo de puntos para cada equipo,
marcadores del juego, información de los jugadores jugador y todos los elementos que se necesitan para administrar de forma eficiente los partidos de futbol que sean organizados.
La idea nació a través de una experiencia personal, a mí me gusta mucho jugar futbol y participo en una pequeña liga en un club deportivo cerca de mi casa, casa fin de semana se asignan juegos y se envía una tabla con los puntos de cada equipo,
sin embargo, en todas las ocasiones había errores o la tabla estaba desactualizada, es por eso que decidí diseñar un sistema que facilitará la gestión de estos juegos y permitirá a cada participante tener una mejor experiencia.

## Características técnicas

<li>Java: 11 </li>
<li>Lombok: 1.18.30 </li>
<li>Mysql-connector-j: 9.0.0</li>

## Uso de la aplicación

Al abrir la aplicación, el usuario (administrador de la cancha) podrá visualizar un panel creado con Java Swing en el que se mostraran distintos elementos que le ayudaran a visualizar, agregar, visualizar y eliminar los juegos, además de 2 botones
que le permitirán navegar dentro de la aplicación para poder interactuar con el resto de información (jugadores y equipos).
![image](https://github.com/user-attachments/assets/cb24c802-3e02-45db-a51a-94d357a0cc00)

Al principio no se visualiza ningún juego, sin embargo, al seleccionar un rango de fecha en el que existan juegos podremos visualizarlos al dar clic en el botón "buscar".
![image](https://github.com/user-attachments/assets/43809de9-e7ff-4efc-8b94-d2ad1001ac90)

Cada juego cuenta con atributos que le permiten al usuario identificarlo y presentarlo a los participantes de la liga, los formularios para agregar, editar y eliminar juegos se presentan a continuación:
*Agregar juego*
![image](https://github.com/user-attachments/assets/c1f9e37f-99c5-4774-a87e-eec2f1e4a680)

*Editar juego*
![image](https://github.com/user-attachments/assets/bae68b56-d013-4e0d-bc9e-3161af819862)

*Eliminar juego*
![image](https://github.com/user-attachments/assets/eb46edd0-e6d6-470d-a007-86b95f1eeda6)

El usuario también puede administrar la información de los jugadores y de sus equipos, utilizando los botones de navegación puede acceder a los paneles que le permitirán agregar, visualizar, editar o eliminar información.

*Panel de jugadores*
![image](https://github.com/user-attachments/assets/61500580-5a5c-4925-9780-b3e02487c456)


*Panel de equipos*
![image](https://github.com/user-attachments/assets/9f39c00f-e80f-4811-8271-d697f97ab36a)

Al igual que en el panel de juegos, el usuario puede buscar, agregar, editar y eliminar información con ayuda de los botones que se encuentran en la parte superior, es importante mencionar que la aplicación cuenta con un sistema que le permite
actualizar los goles a favor, goles en contra y puntos de cada equipo, según fue editado el juego en el que participaron los equipos, esto permite un mayor dinamismo en la captura de datos, haciendo más eficiente y seguro este proceso.
