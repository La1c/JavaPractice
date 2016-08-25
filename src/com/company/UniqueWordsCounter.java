/*
This is a task i solved for the stepic.org Java course.
It can be found here: https://stepic.org/lesson/Stream-API-12781/step/13?course=Java-Базовый-курс&unit=3128

The task itself sounds like this:
You should write a program which would read an UTF-8 text from System.in and get the frequency of each word in it.
In the end it should print 10 most frequent words from the text.
A word is an unbroken sequence of letters and digits.
The program should be case-insensitive and the result should be printed in lower case.

If there is less than 10 different words in the text, print all of them.
If there is two or more words with the same frequency, program should sort them in lexicographic order.

Try to solve it using Stream-API.

Exapmle:

Sample Input:
Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed sodales consectetur purus at faucibus. Donec mi quam, tempor vel ipsum non, faucibus suscipit massa. Morbi lacinia velit blandit tincidunt efficitur. Vestibulum eget metus imperdiet sapien laoreet faucibus. Nunc eget vehicula mauris, ac auctor lorem. Lorem ipsum dolor sit amet, consectetur adipiscing elit. Integer vel odio nec mi tempor dignissim.
________

Sample Output:
consectetur
faucibus
ipsum
lorem
adipiscing
amet
dolor
eget
elit
mi
 */

package com.company;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class UniqueWordsCounter {

    public static void main(String[] args) {

        try(Reader rd = new InputStreamReader(System.in, StandardCharsets.UTF_8);
            BufferedReader reader = new BufferedReader(rd);
            Stream<String> str = reader.lines()){
            Map<String, Integer> myMap;

            Stream<String> str2 = Arrays.stream(str.map(String::toLowerCase)
                    .reduce("", String::concat).split("[^\\p{L}\\p{Digit}_]+"));

            Comparator<Map.Entry<String,Integer>> byNum = (e1,e2) -> e2.getValue().compareTo(e1.getValue());
            Comparator<Map.Entry<String,Integer>> byKey = (e1,e2) -> e1.getKey().compareTo(e2.getKey());
            Comparator<Map.Entry<String,Integer>> myCom = byNum.thenComparing(byKey);

            myMap = str2.collect(Collectors.toMap(w -> w, w -> 1, Integer::sum));
            System.out.println(myMap.size());
            myMap.entrySet().stream().filter(v -> v.getKey().length() > 0).sorted(myCom).limit(10).forEach(v -> System.out.println(v.getKey()));

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
