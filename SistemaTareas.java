import java.util.*;

// Programa: Sistema de Gestión de Tareas pa' la empresa ficticia "Tareitas SA de CV" (osea, tareitas jeje)
// Actividad: Estructuras de Datos - Avance Proyecto Final
// Nota: aqui jugamos con pilas, colas y listas, como si fueran tacos, chelas y amigos

public class SistemaTareas {

    // --- Clase Task = una tarea basica de la empresa
    static class Task {
        private static int NEXT_ID = 1;  // contador de ids
        int id;
        String title;  // titulo de la chamba
        String department; // el depa de la empresa
        int urgency;  // 1 a 5 (5 = ya valio, urgencia full)

        Task(String t, String d, int u) {
            this.id = NEXT_ID++;
            this.title = t;
            this.department = d;
            this.urgency = u;
        }

        @Override
        public String toString() {
            return "["+ id +"] " + title + " (Dept: " + department + ", urg: " + urgency + ")";
        }
    }

    // --- PILA: aqui guardamos las cosas urgentes, tipo cuando el server truena
    static class PilaTareas {
        Deque<Task> stack = new ArrayDeque<>();
        void push(Task t) { stack.push(t); }
        Task pop() { return stack.isEmpty() ? null : stack.pop(); }
        Task peek() { return stack.isEmpty() ? null : stack.peek(); }
        boolean isEmpty() { return stack.isEmpty(); }
        List<Task> toList() { return new ArrayList<>(stack); }
    }

    // --- COLA: pa las cosas que se hacen en fila (como ir por las tortas)
    static class ColaTareas {
        Queue<Task> queue = new ArrayDeque<>();
        void enqueue(Task t) { queue.offer(t); }
        Task dequeue() { return queue.poll(); }
        Task front() { return queue.peek(); }
        boolean isEmpty() { return queue.isEmpty(); }
        List<Task> toList() { return new ArrayList<>(queue); }
    }

    // --- LISTA: aqui lo random, pa buscar o borrar segun el depto
    static class ListaTareas {
        List<Task> list = new ArrayList<>();
        void insert(Task t) { list.add(t); }
        boolean deleteById(int id) {
            for (Iterator<Task> it = list.iterator(); it.hasNext();) {
                Task t = it.next();
                if (t.id == id) { it.remove(); return true; }
            }
            return false;
        }
        Task findById(int id) {
            for (Task t : list) if (t.id == id) return t;
            return null;
        }
        List<Task> toList() { return new ArrayList<>(list); }
    }

    // --- instancias globales, como las areas de la empresa "Tareitas"
    static PilaTareas pila = new PilaTareas();
    static ColaTareas cola = new ColaTareas();
    static ListaTareas lista = new ListaTareas();

    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        // Aqui se simula una pequeña empresa llamada "Tareitas SA de CV"
        // Deptos: Soporte Técnico (alias "Soportón"), Infra (alias "Infracalor"), RRHH (alias "Recursillos Humanos")
        // Todo medio de broma, pero sirve para la actividad

        seedDemo();

        boolean salir = false;
        while (! salir) {
            showMenu();
            String opt = sc.nextLine().trim();
            switch (opt) {
                case "1": addTaskMenu();    break;
                case "2": viewTasksMenu();  break;
                case "3": removeTaskMenu(); break;
                case "4": viewAllByUrgencyDept(); break;
                case "5": testsBasicos();   break;
                case "0": salir = true;     break;
                default:
                    System.out.println("Opción no valida, intenta otra vez\n");
            }
        }
        System.out.println("Gracias por usar el sistema de Tareitas SA, regresa pronto pa más chambitas jeje");
    }

    static void showMenu() {
        System.out.println("\n--- Sistema de Tareas (Tareitas SA) ---");
        System.out.println("1) Agregar tarea (pila/cola/lista)");
        System.out.println("2) Ver tareas (pila/cola/lista)");
        System.out.println("3) Eliminar tarea (pop, dequeue, delete)");
        System.out.println("4) Ver todas las tareas ordenadas por urgencia y depto");
        System.out.println("5) Pruebas rápidas (auto)");
        System.out.println("0) Salir");
        System.out.print("Opcion: ");
    }

    static void addTaskMenu() {
        System.out.print("Título de la tarea: ");
        String title = sc.nextLine().trim();
        System.out.print("Departamento: ");
        String dept = sc.nextLine().trim();
        int urg = readInt("Urgencia (1-5): ", 1, 5);

        System.out.println("Elije estructura: 1=Pila (urgente), 2=Cola, 3=Lista");
        String which = sc.nextLine().trim();
        Task t = new Task(title, dept, urg);
        switch (which) {
            case "1": pila.push(t);  System.out.println("Tarea a la pila: " + t); break;
            case "2": cola.enqueue(t); System.out.println("Tarea a la cola: " + t); break;
            case "3": lista.insert(t); System.out.println("Tarea en lista: " + t); break;
            default: System.out.println("Opción no valida, no se guardo nada");
        }
    }

    static void viewTasksMenu() {
        System.out.println("\n-- PILA (urgentes) --");
        for (Task t : pila.toList()) System.out.println(t);
        System.out.println("\n-- COLA (programadas) --");
        for (Task t : cola.toList()) System.out.println(t);
        System.out.println("\n-- LISTA (por depto) --");
        for (Task t : lista.toList()) System.out.println(t);
    }

    static void removeTaskMenu() {
        System.out.println("Elige: 1=pop pila, 2=dequeue cola, 3=delete por id (lista)");
        String which = sc.nextLine().trim();
        switch (which) {
            case "1": {
                Task t = pila.pop();
                System.out.println(t == null ? "Pila vacia" : "Se saco: " + t);
                break;
            }
            case "2": {
                Task t = cola.dequeue();
                System.out.println(t == null ? "Cola vacia" : "Se proceso: " + t);
                break;
            }
            case "3": {
                int id = readInt("ID a eliminar: ", 1, Integer.MAX_VALUE);
                boolean ok = lista.deleteById(id);
                System.out.println(ok ? "Eliminado." : "No se encontro ID");
                break;
            }
            default: System.out.println("Opción invalida.");
        }
    }

    static void viewAllByUrgencyDept() {
        List<Task> all = new ArrayList<>();
        all.addAll(pila.toList());
        all.addAll(cola.toList());
        all.addAll(lista.toList());
        all.sort((a,b) -> {
            int c = Integer.compare(b.urgency, a.urgency);
            if (c != 0) return c;
            return a.department.compareToIgnoreCase(b.department);
        });

        System.out.println("\n--- Todas las tareas ordenadas ---");
        for (Task t : all) System.out.println(t);
    }

    static int readInt(String prompt, int min, int max) {
        while (true) {
            System.out.print(prompt);
            String line = sc.nextLine().trim();
            try {
                int v = Integer.parseInt(line);
                if (v < min || v > max) System.out.println("Fuera de rango, intenta de nuevo");
                else return v;
            } catch (Exception e) {
                System.out.println("Eso no es numero bro");
            }
        }
    }

    static void seedDemo() {
        pila.push(new Task("Arreglar fallo critico del server", "Soportón", 5));
        pila.push(new Task("Reiniciar router del Infracalor", "Infra", 5));
        cola.enqueue(new Task("Reporte semanal de Recursillos", "RRHH", 2));
        cola.enqueue(new Task("Hacer backup de la nube nublada", "Infra", 3));
        lista.insert(new Task("Revisar dinerillos del mes", "Finanzas Locochonas", 2));
        lista.insert(new Task("Contratar becario baratito", "RRHH", 1));
    }

    static void testsBasicos() {
        System.out.println("\n-- Test rapido --");
        System.out.println("Peek pila: " + pila.peek());
        System.out.println("Front cola: " + cola.front());
        System.out.println("Find en lista id 1: " + lista.findById(1));
        System.out.println("(Si algo sale null, mete mas tareitas primero)");
    }

}
