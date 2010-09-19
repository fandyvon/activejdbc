/*
Copyright 2009-2010 Igor Polevoy

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/
package javalite.common;

import java.util.*;

/**
 * @author Igor Polevoy
 */
public class Collections {

    public static <T> T[] arr(T... values) {
        return values;
    }

    public static <T> Set<T> set(T... ts) {
        return new HashSet<T>(Arrays.asList(ts));
    }

    @SuppressWarnings("unchecked")
    public static <T, K> Map<T, K> map(Object... values) {
        if (values.length % 2 != 0) throw new IllegalArgumentException("number of arguments must be even");

        Map<T, K> result = new HashMap<T, K>();
        for (int i = 0; i < values.length; i += 2) {
            result.put((T) values[i], (K) values[i + 1]);
        }
        return result;
    }
}
