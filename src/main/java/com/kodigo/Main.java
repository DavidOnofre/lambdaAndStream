package com.kodigo;

import com.kodigo.model.Person;
import com.kodigo.model.Product;

import java.time.LocalDate;
import java.time.Period;
import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Main {


    private static List<Person> persons;

    private static List<Product> products;


    public static void main(String[] args) {
        initValues();
        //m1_ReccorrerUnaLista();
        //m2_filtrar();
        //m4_Map();
        //m5_Sorted();
        //m6_match_anyMatch();
        //m7_match_allMatch();
        //m7_match_noneMatch();
        //m8_LimitSkip();
        //m8_LimitSkip_ComoPaginacion();
        //m9_collectors();
        //m9_collectors_counting();
        //m10_AgrupandoPorNombre();
        //m11_Estadistica();
        m12_reduce();
    }

    // agrupar algunos criterios como acumuladores (sumas, min, max)
    // reduce retorna un optional
    // optional desde java8, para evitar nullPointException
    private static void m12_reduce() {
        Optional<Double> sum = products.stream()
                .map(Product::getPrice)
                .reduce(Double::sum); // .reduce((a,b) -> a+b)

        System.out.println(sum.get()); // para obtener la informacion dentro de un optional uso .get()
    }

    // metodo que devuelve max, min, promedio, de todos los precios
    // devuelve un resumen estadistico
    private static void m11_Estadistica() {
        DoubleSummaryStatistics statistics = products.stream()
                .collect(Collectors.summarizingDouble(Product::getPrice));

        System.out.println(statistics);

        // si quiero obtener un valor particular del resumen estadistico
        System.out.println(statistics.getMax());

    }

    // en este ejemplo se esta agrupando por nombre
    // y se esta sumando sus precios
    private static void m10_AgrupandoPorNombre() {
        Map<String, Double> collect3 = products.stream()
                .collect(Collectors.groupingBy(
                                Product::getName,
                                Collectors.summingDouble(Product::getPrice)
                        )
                );

        System.out.println(collect3);
    }

    // en este ejemplo me muestra los productos repetidos por nombre
    // muestra cuantos existen
    private static void m9_collectors_counting() {

        // String es por que es el criterio (nombre)
        // y el resultado de la agrupacion es un valor numerico (long)
        Map<String, Long> collect2 = products.stream()
                .collect(Collectors.groupingBy(
                                Product::getName, Collectors.counting()
                        )
                );

        System.out.println(collect2);
    }

    // group by
    // filtrar los productos cuyo precio sea mayor a 3
    // ademas los agrupo por su nombre
    private static void m9_collectors() {
        Map<String, List<Product>> collect1 = products.stream()
                .filter(p -> p.getPrice() > 3)
                .collect(Collectors.groupingBy(Product::getName));

        System.out.println(collect1);
    }

    private static void m8_LimitSkip_ComoPaginacion() {
        int pageNumber = 2;
        int pageSize = 2;
        List<Person> filteredList4 = persons.stream()
                .skip(pageNumber * pageSize) //
                .limit(pageSize)
                .collect(Collectors.toList());

        printList(filteredList4);
    }

    // skip: si son 5 elementos a partir del 2 los va a conciderar (imprime a partir del 2)
    // limit: del flujo de coleccion que tengo solo va a considerar los 2 primeros
    private static void m8_LimitSkip() {
        List<Person> filteredList4 = persons.stream()
                //.skip(2)
                .limit(2)
                .collect(Collectors.toList());

        printList(filteredList4);
    }

    // noneMatch: evalua tod0 el stream bajo la condición
    // ninguno debe elemento del stream debe iniciar con la J
    private static void m7_match_noneMatch() {
        // 1.
        boolean rpta1 = persons.stream()
                .noneMatch(p -> p.getName().startsWith("J"));
        System.out.println(rpta1);

        // 2.
        Predicate<Person> startWithPredicate = p -> p.getName().startsWith("J");
        boolean rpta2 = persons.stream()
                .noneMatch(startWithPredicate);
        System.out.println(rpta2);
    }

    // allMatch: evalua tod0 el stream bajo la condición
    private static void m7_match_allMatch() {
        // 1.
        boolean rpta1 = persons.stream()
                .allMatch(p -> p.getName().startsWith("J"));
        System.out.println(rpta1);

        // 2.
        Predicate<Person> startWithPredicate = p -> p.getName().startsWith("J");
        boolean rpta2 = persons.stream()
                .allMatch(startWithPredicate);
        System.out.println(rpta2);
    }

    // No evalua tod0 el stream, termina en la coincidencia
    // termina cuando encuentre la primera coincidencia
    // no espera recorrer tod0 el stream
    private static void m6_match_anyMatch() {
        // 1.
        boolean rpta1 = persons.stream()
                .anyMatch(p -> p.getName().startsWith("J"));
        System.out.println(rpta1);

        // 2.
        Predicate<Person> startWithPredicate = p -> p.getName().startsWith("J");
        boolean rpta2 = persons.stream()
                .anyMatch(startWithPredicate);
        System.out.println(rpta2);
    }

    // para poder ordenar a través de una colección
    // Stored(param: Comparator)
    private static void m5_Sorted() {

        Comparator<Person> byNameAsc = (Person p1, Person p2) -> p1.getName().compareTo(p2.getName());
        Comparator<Person> byNameDesc = (Person p1, Person p2) -> p2.getName().compareTo(p1.getName());

        List<Person> filteredList3 = persons.stream()
                .sorted(byNameAsc)
                .collect(Collectors.toList());

        printList(filteredList3);
    }

    private static void m4_Map() {

        /*List<String> filteredList2 = persons.stream()
                .map(p -> "Koder " + p.getName())
                .collect(Collectors.toList());*/

        // puedo crear la logica fuction por separado
        // luego usarla en mi lista
        // koderFunction es equivalente a
        // .map(p -> "Koder " + p.getName())

        Function<String, String> koderFunction = name -> "Koder" + name;

        List<String> filteredList2 = persons.stream()
                .map(Person::getName)
                // .map(Person::getName)
                // es neceasrio ya que hasta antes de este punto el stream es
                // de personas, pero mi koderFuction requiere stream de string
                // p -> p.getName() es lo mismo que Person::getName
                .map(koderFunction)
                .collect(Collectors.toList());

        printList(filteredList2);
    }

    // MAP es un metodo que me permite transformar los elementos de la coleccion (o stream)
    // es utilizado cuando se quiere transaformar de un valor a otro valor usando
    // algun tipo de lógica
    // Map (param: Function) (fuction no espera un retorno de tipo boolean, sino un valor producto de la condicion
    //                       indicada en el function)
    private static void m3_Map() {
        List<Integer> filteredList2 = persons.stream()
                .map(p -> getAge(p.getBirthDate()))
                .collect(Collectors.toList());
        // cuando recollecto con Collector.toList()
        // recupero los valores del último operador
        // para  este caso el getBirhDate que retorna integer

        printList(filteredList2);
    }

    // SELECT * FROM PERSONA p WHERE p.edad > 18;
    // se esta filtrando con paradigma funcional
    // no me interesa como se programa el where
    // me interesa que mis datos cumplan con una condicion
    // Filter(param: Predicate) (predicate es una expresion que te retorna una expresión boolean)
    private static void m2_filtrar() {
        // stream es un metodo que me permite trabajar de una forma
        // declarativa con colecciones y operar sobre ellas

        List<Person> filteredList = persons.stream()
                .filter(p -> getAge(p.getBirthDate()) >= 29) // como salida del filter tengo un nuevo flujo
                .collect(Collectors.toList()); // para poder usar esa salida, colecciono sa salida del flujo por el filter

        printList(filteredList);
    }

    private static void m1_ReccorrerUnaLista() {
        // forma imperativa
        /*for (int i = 0; i < persons.size(); i++) {
            System.out.println(persons.get(i));
        }*/

        // recorrer usando foreach
        // mas o menos hasta 2013-2014
        /*for (Person person: persons) {
            System.out.println(person);
        }*/

        // el resultado anterior se lo podria obtener con
        // la siguiente expresion lambda: p-> System.out.println(p),
        // que a su vez es lo mismo que usar un metodo de referencia
        // esta caracteristica fue lanzada partir de java 8 ::
        persons.forEach(System.out::println);
    }

    //metodo para recuperar la edad de una persona
    public static int getAge(LocalDate birthDate) {
        return Period.between(birthDate, LocalDate.now()).getYears();
    }

    // metodo usado para imprimir una lista
    // ? para poder recibir listas de cualquier tipo
    public static void printList(List<?> list) {
        list.forEach(System.out::println);
    }

    private static void initValues() {
        Person person1;
        Person person2;
        Person person3;
        Person person4;
        Person person5;

        Product product1;
        Product product2;
        Product product3;
        Product product4;
        Product product5;
        Product product6;

        person1 = Person
                .builder()
                .id(1)
                .name("David")
                .birthDate(LocalDate.of(1988, 11, 3))
                .build();

        person2 = Person
                .builder()
                .id(2)
                .name("Jonatan")
                .birthDate(LocalDate.of(1990, 2, 20))
                .build();

        person3 = Person
                .builder()
                .id(3)
                .name("Estefania")
                .birthDate(LocalDate.of(1993, 9, 15))
                .build();

        person4 = Person
                .builder()
                .id(4)
                .name("Priscila")
                .birthDate(LocalDate.of(1995, 4, 20))
                .build();

        person5 = Person
                .builder()
                .id(5)
                .name("Patricio")
                .birthDate(LocalDate.of(1998, 3, 20))
                .build();

        product1 = new Product(1, "Ceviche", 5.50);
        product2 = new Product(2, "Pollo con Papas", 2.25);
        product3 = new Product(3, "Pescado", 3.50);
        product4 = new Product(4, "Arroz con Menestra", 3.25);
        product5 = new Product(5, "Pinchos", 0.80);
        product6 = new Product(1, "Ceviche", 5.60);

        persons = Arrays.asList(person1, person2, person3, person4, person5);
        products = Arrays.asList(product1, product2, product3, product4, product5, product6);

    }
}