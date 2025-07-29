# 🧠 Resolución de Laberintos con Algoritmos de Búsqueda

<table>
  <tr>
    <td><img src="https://yt3.ggpht.com/a/AATXAJxzy1qiMNCOThCYIp3j_4O34ALZBF4xqOaxIQ=s900-c-k-c0xffffffff-no-rj-mo" alt="Logo UPS" width="100"/></td>
    <td>
      <h1>Universidad Politécnica Salesiana</h1>
    </td>
  </tr>
</table>

## **Materia:** Estructura de Datos  
## **Autores:**  
- Jaime Ismael Loja Tenesaca – `jlojat2@est.ups.edu.ec`  
- Guillermo Daniel Cajas Ortega – `gcajaso@est.ups.edu.ec`  
- Pablo Esteban Escandón Lema – `pescandonl@est.ups.edu.ec`  
- Kevin Andrés Paladines Toledo – `kpaladinest@est.ups.edu.ec`

---

## 📌 Descripción del Problema

El proyecto tiene como objetivo resolver laberintos de forma automática utilizando diversos algoritmos de búsqueda. Para ello, se proporciona una interfaz gráfica que permite al usuario diseñar el laberinto, establecer el punto de inicio y fin, y seleccionar el algoritmo a utilizar.

Este entorno permite observar el recorrido de cada algoritmo, su eficiencia y el camino final que logra trazar desde el inicio hasta el objetivo.

---

## 💡 Propuesta de Solución

Se implementó un sistema basado en el patrón Modelo-Vista-Controlador (MVC), en lenguaje Java, con una interfaz gráfica desarrollada en Swing. Se desarrollaron e integraron diversos algoritmos de búsqueda para la resolución de laberintos, cada uno con su propia estrategia de exploración:

- Recursivo con 2 direcciones

- Recursivo con 4 direcciones

- BFS (Breadth-First Search)

- DFS (Depth-First Search)

- Recursivo 4 direcciones con backtraking

El sistema permite además realizar la visualización paso a paso del recorrido y obtener estadísticas como tiempo y número de celdas exploradas.
---
### 📚 Marco Teórico

- **DFS (Depth-First Search):** Explora en profundidad un camino hasta llegar al final o atascarse, retrocediendo si es necesario (pila). Puede no encontrar la solución óptima, pero es fácil de implementar.  
- **BFS (Breadth-First Search):** Explora por niveles desde el punto de inicio. Garantiza encontrar el camino más corto, pero consume más memoria (cola).  
- **Backtracking Recursivo:** Prueba todas las posibles rutas utilizando retroceso. Guarda el camino y retrocede si se encuentra con un obstáculo o una ruta peor.  
- **Recursivo simple (mínimo):** Intenta ir solo hacia abajo o hacia la derecha, lo que reduce la complejidad, pero no siempre encuentra una solución si el camino está bloqueado.

---

### ⚙️ Tecnologías Utilizadas

- **Lenguaje:** Java  
- **Interfaz gráfica:** Swing  
- **Patrón de diseño:** Modelo-Vista-Controlador (MVC)  
- **Animación de recorrido:** `Consumer<Cell>` para callback visual  
- **Programación orientada a objetos**

---

### 📐 Diagrama UML (simplificado)
    // falta esto
---

### 🖼️ Capturas de la Interfaz

#### 🔸 Ejemplo 1 – Algoritmo: Recursivo 2 direcciones 
    // Si quieren poner otro metodo cambien los titulos..
#### 🔸 Ejemplo 2 – Algoritmo: Recursivo 4 direcciones

---

### 💻 Código Comentado – Ejemplo: BFS

```java
@Override
public MazeResult getPath(boolean[][] grid, Cell start, Cell end, Consumer<Cell> callback) {
    int rows = grid.length;
    int cols = grid[0].length;
    boolean[][] visited = new boolean[rows][cols];
    Map<Cell, Cell> parent = new HashMap<>();

    Queue<Cell> queue = new LinkedList<>();
    queue.add(start);
    visited[start.getRow()][start.getCol()] = true;

    // Direcciones: abajo, arriba, derecha, izquierda
    int[][] directions = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};

    while (!queue.isEmpty()) {
        Cell current = queue.poll();

        // Visualizar celda explorada en UI
        if (callback != null && uiGrid != null) {
            callback.accept(uiGrid[current.getRow()][current.getCol()]);
            try {
                Thread.sleep(30); // Delay para visualizar recorrido
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        // Si se llegó al final
        if (current.equals(end)) {
            List<Cell> path = buildPath(parent, end);
            return new MazeResult(path, null);
        }

        // Explorar vecinos
        for (int[] dir : directions) {
            int newRow = current.getRow() + dir[0];
            int newCol = current.getCol() + dir[1];

            // Validar que esté dentro de los límites y no visitado
            if (newRow >= 0 && newRow < rows && newCol >= 0 && newCol < cols &&
                grid[newRow][newCol] && !visited[newRow][newCol]) {

                Cell neighbor = new Cell(newRow, newCol);
                queue.add(neighbor);
                visited[newRow][newCol] = true;
                parent.put(neighbor, current);
            }
        }
    }

    return new MazeResult(null, "No se encontró un camino");
}
 
 ```
---
## 📝 Conclusiones
#### Daniel Cajas:
En conclusión este proyecto me ayudó a comprender en profundidad cómo diferentes algoritmos de búsqueda abordan la resolución de laberintos, destacando sus ventajas y limitaciones en eficiencia y complejidad. La integración con una interfaz gráfica interactiva permitió visualizar el proceso en tiempo real, lo que facilitó tanto la depuración como la experiencia del usuario. Además, fue una excelente oportunidad para aplicar conceptos de estructuras de datos y programación en Java en un contexto práctico y visual.

#### Jaime Loja:

#### Kevin Paladines:

#### Pablo Escandón:

--- 

## 🧭 Recomendaciones y Aplicaciones Futuras

- Se podría integrar una **métrica de rendimiento** para contar pasos, tiempo y memoria utilizada por cada algoritmo.
- Ampliar los algoritmos con **A\*** o **Dijkstra** para resolver laberintos más complejos.
- Incorporar **generación automática de laberintos aleatorios**.
- Permitir **exportar el camino resultante** como imagen o archivo para documentación o análisis.
- Implementar una **versión web en JavaScript (Canvas)** para mejorar la accesibilidad y portabilidad de la herramienta.

