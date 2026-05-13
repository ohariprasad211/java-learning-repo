package com.haridev;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Interview {
    public static void main(String[] args) {
        List<Object> listObj = Arrays.asList(1, 4, 2, 3, 6, 'r', 'a');
        listObj.stream().map(o -> {
            if (o instanceof Integer) {
                return (int) o;
            } else if (o instanceof Character) {
                return (int)((Character) o);
            } else {
                return -1;
            }
        }).sorted(Integer::compareTo)
                .map(ascii->{
                    if (ascii >= 48 && ascii <= 57) {
                        return (int) ascii.intValue();
                    } else {
                        return (Object) (char) ascii.intValue();
                    }
                })
                        .forEach(System.out::println);
/*        for (int i = 0; i < listObj.size(); i++) {
            System.out.println(listObj.get(i));
//            if()
        }*/
    }
}
