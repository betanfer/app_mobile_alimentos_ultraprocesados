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

Nuestra aplicación está orientada para niños de 13 años de edad, que vienen/residen en la argentina donde la misma puede implementarse en el colegio principalmente en un actividad en la materia de biología.
Se puede utilizar tanto con una mano como con las dos, la idea es que los juegos no duren tanto tiempo y el usuario no se aburra entre jugos.
Los juegos favoritos de los niños de este rango etario son Minecraft y Roblox.
Cuando los botones son muy chicos, cuando los colores no tienen una armonía, cuando el juego es principalmente para pc y no se adapta bien para el teléfono.

Nuestro user persona es un chico de secundaria llamado Mateo. Él tiene 13 años, vive en Argentina y acude todos los días a un colegio secundario. El colegio de Mateo tiene doble jornada, por lo que el chico come muchas de sus comidas diarias en el colegio y, en general, sus padres le dan plata para que se compre lo que él quiere en el quiosco del colegio.  
Cuando no está en el colegio, Mateo pasa la mayor parte de su tiempo en redes sociales como Tiktok y Youtube y también juega muchos juegos online, sobre todo en la plataforma android, en el smartphone que le regalaron sus padres cuando comenzó el secundario para poder comunicarse con ellos. También juega algunos juegos de PC, sobre todo Minecraft y Roblox.
A Mateo le gusta mucho educación física y plástica, pero le cuestan otras materias porque tiene algunos problemas para concentrarse, distraído por las constantes imágenes coloridas de las pantallas, muchas veces le cuesta leer o simplemente se saltea los textos que tiene enfrente para concentrarse en las visuales. 

## Colores

* **#172929 - Fondo Principal:** Este tono oscuro profundo es la base de toda la interfaz. Elegimos un fondo oscuro para reducir la fatiga visual en sesiones de juego y para que los colores del semáforo y los textos claros resalten con fuerza sobre él. Además, genera una estética moderna que conecta con los juegos que ya consume nuestro público.
* **#E7F1F2 - Texto Primario:** Este tono hielo/casi blanco es el color principal de lectura. Sobre el fondo oscuro ofrece un contraste alto y limpio, ideal para títulos, párrafos y toda la información que el jugador necesita leer cómodamente.
* **#86BCBD - Texto Secundario y Elementos de Apoyo:** Este turquesa suave lo usamos como segundo nivel de texto y para elementos de la UI que acompañan sin competir: subtítulos, etiquetas, hints en campos de texto, bordes decorativos y botones secundarios (como el de menú o configuración).
* **#A4CE8B - Texto Resaltado y Éxito (Verde Semáforo):** Este verde pastel cumple doble función: por un lado resalta la información importante dentro de los textos (highlights), y por el otro es el color principal de los botones de acción ("Jugar", "Aceptar", "Siguiente") y el indicador de éxito en el sistema semáforo. Es nuestro verde elegido para marcar las elecciones saludables.
* **#F7E49B - Advertencia (Amarillo Semáforo):** Amarillo cálido para el nivel intermedio del semáforo. Marca las opciones "normales" que no son ni las más saludables ni las peores. También lo usamos para avisos y estados de atención.
* **#BA5A5A - Error y Alertas (Rojo Semáforo):** Rojizo terroso para contrastar fuertemente y llamar la atención en situaciones críticas: indicadores de alimentos poco saludables, notificaciones de error y el nivel rojo del semáforo.

La paleta de colores fue pensada con tres cosas en mente. Primero, que sea atractiva y con personalidad para conectar con el público joven, usando colores naturales que se relacionan con los grupos alimenticios y la naturaleza. Segundo, que genere un contraste alto y claro entre fondo oscuro, texto claro y colores de acento, respetando las normativas de accesibilidad. Por último, pensamos añadir dos paletas de colores extras para dos "modos daltónicos" diferentes, implementadas como temas alternativos que el usuario puede activar desde la configuración de la app.

### Paleta para Protanopia / Deuteranopia

| Color original | Equivalente | Uso |
|:-:|:-:|:--|
| #BA5A5A | #90905A | Error / Semáforo rojo |
| #F7E49B | #EEEFAD | Advertencia / Semáforo amarillo |
| #A4CE8B | #B7B79B | Éxito / Semáforo verde / Botón primario |
| #86BCBD | #9D9EBD | Texto secundario / Botón secundario |
| #E7F1F2 | #EBEBF3 | Texto primario |
| #172929 | #1F1F29 | Fondo |

### Paleta para Tritanopia

| Color original | Equivalente | Uso |
|:-:|:-:|:--|
| #BA5A5A | #B5595A | Error / Semáforo rojo |
| #F7E49B | #F6BBBF | Advertencia / Semáforo amarillo |
| #A4CE8B | #A7A8AC | Éxito / Semáforo verde / Botón primario |
| #86BCBD | #89BCBD | Texto secundario / Botón secundario |
| #E7F1F2 | #E8F2F2 | Texto primario |
| #172929 | #192929 | Fondo |

Link a la paleta de colores: [Paleta](https://palettechecker.com/#BA5A5A-F7E49B-A4CE8B-86BCBD-E7F1F2-172929)

## Tipografías

Para la tipografía elegimos OpenDyslexic, ya que es una tipografía divertida, con personalidad, que facilita la lectura a personas disléxicas y con problemas de atención. Además es gratuita y Open Source. Está definida como fuente global del tema, aplicándose a todos los textos de la app automáticamente.

Se definieron cuatro niveles de tamaño de texto:
* **Títulos (28sp):** Para pantallas principales, nombres de secciones y resultados de partida.
* **Subtítulos (20sp):** Para encabezados de sección dentro de una pantalla, como las comidas del menú.
* **Texto general (16sp):** Para descripciones, datos de alimentos, sección "¿Sabías qué?" y textos informativos.
* **Texto de botón (18sp):** Para el texto dentro de los botones, lo suficientemente grande para leerse sin esfuerzo.

Documentación oficial:
[OpenDyslexic](https://opendyslexic.org/)

## Botones

Los botones tienen esquinas redondeadas y la tipografía OpenDyslexic sin mayúsculas forzadas, para mantener la legibilidad y la personalidad visual de la app.

Los **botones de acción principal** (como "Jugar", "Aceptar", "Siguiente" o los accionables dentro del juego) usan el color verde de éxito (#A4CE8B) con texto oscuro (#172929). Ocupan el ancho completo disponible de la pantalla con márgenes laterales para no tocar los bordes, son rectangulares con esquinas redondeadas y se posicionan en la parte inferior de la pantalla para que sean de fácil acceso para el usuario.

Los **botones secundarios** (como configuración, opciones, pausar el juego) usan el color turquesa (#86BCBD) con texto oscuro. Son más pequeños, con una proporción cercana a 1:1 (casi cuadrados), también con bordes redondeados. Se ubican en la parte superior derecha de la pantalla porque es la posición acostumbrada para este tipo de acciones en otros juegos, es la posición a la que el usuario ya está acostumbrado. Son lo suficientemente grandes como para que puedan ser accionados con facilidad sin tocar sin querer otra parte de la pantalla, pero lo suficientemente compactos como para no molestar ni distraer al usuario.

## Algunos recursos gratuitos que usamos como inspiración

[Botones](https://www.figma.com/design/ppNLYbER8ipWWv9QGg1O5h/Trendy-Buttons--Community-?node-id=0-1&t=6PhVaQbMiiN6wuf6-1)
[Botones](https://www.figma.com/design/wMrYdqKOV5LTSREcoZhzVp/Interactive-Buttons-set-by-Nikica--Community-?node-id=1-201&p=f)
[Interfaz](https://www.figma.com/design/iT747ijREVzySCe8EmBYUf/Shoping-App-Iphone--Community-?node-id=0-1&t=nuA7Iqe7vNznlauL-1)