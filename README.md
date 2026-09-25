## Nombre del proyecto: 

Nutri-life app: Código Verde

## Nombre del proyecto: 

Grupo 2

## Integrantes:

Betancor Fernando
Rey Lucas
Rodríguez Stephany
Scodelari Julia

## Figma con ideas de diseños:

[Figma](https://www.figma.com/design/0FFUgH8JY8tdWQKZwHYNxw/C%C3%B3digo-Verde?node-id=0-1&t=GePfTl6c2rRsv8mP-1)

## 1. Definimos nuestra persona usuaria

Describan brevemente a la persona para la que están diseñando el juego.
Incluyan:
* **Edad y contexto:** dónde vive, qué hace habitualmente y en qué situaciones podría jugar.
* **Uso del celular:** si suele utilizarlo con una o dos manos, en qué lugares lo usa y durante cuánto tiempo.
* **Preferencias:** qué tipos de juegos y aplicaciones utiliza o le gustan.
* **Necesidades y dificultades:** qué aspectos de los juegos o aplicaciones actuales pueden resultarle incómodos o molestos.

La descripción debe ser concreta y estar relacionada con el tipo de juego que están desarrollando.

Nuestra aplicación está orientada para niños de 13 años de edad, que vienen/residen en la argentina donde la misma puede implementarse en el colegio principalmente en un actividad en la materia de biología.
Se puede utilizar tanto con una mano como con las dos, la idea es que los juegos no duren tanto tiempo y el usuario no se aburra entre jugos.
Los juegos favoritos de los niños de este rango etario son Minecraft y Roblox.
Cuando los botones son muy chicos, cuando los colores no tienen una armonía, cuando el juego es principalmente para pc y no se adapta bien para el teléfono.

Nuestro user persona es un chico de secundaria llamado Mateo. Él tiene 14 años, vive en Argentina y acude todos los días a un colegio secundario. El colegio de Mateo tiene doble jornada, por lo que el chico come muchas de sus comidas diarias en el colegio y, en general, sus padres le dan plata para que se compre lo que él quiere en el quiosco del colegio.  
Cuando no está en el colegio, Mateo pasa la mayor parte de su tiempo en redes sociales como Tiktok y Youtube y también juega muchos juegos online, sobre todo en la plataforma android, en el smartphone que le regalaron sus padres cuando comenzó el secundario para poder comunicarse con ellos. También juega algunos juegos de PC, sobre todo Minecraft y Roblox.
A Mateo le gusta mucho educación física y plástica, pero le cuestan otras materias porque tiene algunos problemas para concentrarse, distraído por las constantes imágenes coloridas de las pantallas, muchas veces le cuesta leer o simplemente se saltea los textos que tiene enfrente para concentrarse en las visuales. 

## Colores

Definan la paleta que utilizará la aplicación.
Para cada color indiquen:
* Nombre o función del color.
* Código RGB.
* Dónde se utilizará.

Por ejemplo: fondo, botones, textos, elementos destacados, estados, etc.

* **--color-5 (#E7F1F2) - Fondo Principal:** Este tono hielo/casi blanco es ideal para el fondo general del juego o de los menús. Mantiene la pantalla limpia, luminosa y permite que los demás colores destaquen sin saturar la vista. Además, los fondos claros son mejores para utilizar en condiciones de mucha luz y nuestro juego está pensado para jugarse en la escuela, en compañía de otras personas, es decir, en condiciones de buena iluminación. 
* **--color-6 (#172929) - Texto y Contornos:** Este tono oscuro fue elegido principalmente para la legibilidad. Lo pensamos para tipografías, títulos e íconos. Ofrece un gran contraste sobre el fondo claro.
* **--color-2 (#F7E49B) - Protagonista Alegre (Paneles y Botones Principales):** Para darle a la app una vibra juvenil sin arruinar el contraste, lo pensamos para los grandes bloques de la UI: fondos de ventanas emergentes (pop-ups), botones de "Jugar" o marcos.  
* **--color-3 (#A4CE8B) - Acciones Positivas y Energía:** Este verde pastel es perfecto para botones de confirmación ("Aceptar", "Siguiente"), indicadores de éxito, o barras de estamina/salud. Nuestra app plantea un “sistema semáforo” (rojo, amarillo, verde) para marcar claramente las elecciones buenas y malas de alimentos. Este es nuestro verde elegido. 
* **--color-1 (#BA5A5A) - Alertas y Cancelaciones:** Rojizo terroso para contrastar fuertemente y llamar la atención en interacciones críticas: botones de "Salir", "Cerrar", notificaciones de error o indicadores de alimentos poco saludables. 
* **--color-4 (#86BCBD) - Elementos Secundarios:** Este turquesa lo elegimos como color de contraste y de apoyo. Lo pensamos para botones inactivos o menos importantes, bordes decorativos, pestañas no seleccionadas o barras de progreso de carga, que acompañe al amarillo sin robarle el foco. 

La paleta de colores fue pensada con tres cosas en mente, que sea alegre y colorida para que vaya bien con el público joven. Además, la variedad de colores hace referencia a los diferentes grupos alimenticios, elegimos una paleta de colores naturales que se relaciona con elementos de la naturaleza. Además, esta pensada para generar un buen contraste entre colores de texto, acento y fondo, para respetar las normativas de accesibilidad. Por último, pensamos añadir dos paletas de colores extras para dos “modos daltónicos” diferentes, y esta paleta en particular ofrecía una buena cantidad de colores tanto en “modo normal” como en “modo daltónico”.

## Tipografías

Definan:
* Tipografía para títulos.
* Tipografía para textos.
* Tamaño de cada una.

Para la tipografía elegimos OpenDyslexic, ya que es una tipografía divertida, con personalidad, que facilita la lectura a personas disléxicas y con problemas de atención. Además es gratuita y Open Source.

Documentación oficial:
[OpenDyslexic](https://opendyslexic.org/)

## Botones

Definan cómo serán los botones de la aplicación:
* Forma.
* Tamaño.
* Color.
* Texto o elemento que contendrán.
* Ubicación aproximada dentro de la pantalla.

Tengan especialmente en cuenta la ergonomía del pulgar y las zonas de fácil acceso en dispositivos móviles.

Los botones serán semi redondeados (los diseños de referencia se encuentran en el Figma y en la carpeta de imágenes dentro de este repositorio).
Los botones de acciones principales, como por ejemplo “jugar” o los mismo accionables dentro del juego, se posicionarán en la parte inferior de la pantalla para que sean de fácil acceso para el usuario, serán rectangulares con esquinas redondeadas y ocuparán de tamaño el tercio central del tamaño disponible. 
Los botones de menús (como configuraciones, opciones, botones para pausar el juego), se ubicarán en la parte superior derecha de la pantalla porque es la posición acostumbrada para este tipo de acciones en otros juegos, es la posición a la que el usuario ya está acostumbrado. Estos serán más cuadrados (es decir con un alto y ancho similar), también semi redondeados y pequeños para no molestar ni distraer al usuario, pero lo suficientemente grandes como para que puedan ser accionados con facilidad sin tocar sin querer otra parte de la pantalla.

## Algunos recursos gratuitos que usamos como inspiración

[Botones](https://www.figma.com/design/ppNLYbER8ipWWv9QGg1O5h/Trendy-Buttons--Community-?node-id=0-1&t=6PhVaQbMiiN6wuf6-1)
[Botones](https://www.figma.com/design/wMrYdqKOV5LTSREcoZhzVp/Interactive-Buttons-set-by-Nikica--Community-?node-id=1-201&p=f)
[Interfaz](https://www.figma.com/design/iT747ijREVzySCe8EmBYUf/Shoping-App-Iphone--Community-?node-id=0-1&t=nuA7Iqe7vNznlauL-1)