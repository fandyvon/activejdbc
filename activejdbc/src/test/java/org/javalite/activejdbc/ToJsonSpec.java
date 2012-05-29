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

package org.javalite.activejdbc;


import org.javalite.activejdbc.test.ActiveJDBCTest;
import org.javalite.activejdbc.test_models.*;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Test;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author Igor Polevoy
 */
public class ToJsonSpec extends ActiveJDBCTest {
    ObjectMapper mapper = new ObjectMapper();

    @Test
    public void shouldGenerateSimpleJson() throws IOException {
        deleteAndPopulateTable("people");
        Person p  = Person.findById(1);
        //test no indent
        String json = p.toJson(false, "name", "last_name", "dob");
        mapper.readTree(json);//check validity
        the(json).shouldBeEqual("{\"dob\":\"1934-12-01\",\"last_name\":\"Smith\",\"name\":\"John\"}");
        //test indent
        json = p.toJson(true, "name", "last_name", "dob");
        mapper.readTree(json);//check validity
        String expected = "{\n" +
                "  \"dob\":\"1934-12-01\",\n" +
                "  \"last_name\":\"Smith\",\n" +
                "  \"name\":\"John\"\n" +
                "}";
        the(json).shouldBeEqual(expected);
    }

    @Test
    public void shouldIncludePrettyChildren() throws IOException {
        deleteAndPopulateTables("users", "addresses");
        List<User> personList = User.findAll().orderBy("id").include(Address.class);
        User u = personList.get(0);
        String json = u.toJson(true);
        mapper.readTree(json);//check validity
        a(json).shouldEqual("{\"model_class\":\"org.javalite.activejdbc.test_models.User\",\n" +
                "  \"id\":\"1\",\n" +
                "  \"first_name\":\"Marilyn\",\n" +
                "  \"email\":\"mmonroe@yahoo.com\",\n" +
                "  \"last_name\":\"Monroe\",\n" +
                "  \"children\" : {\n" +
                "    \"addresses\" : [\n" +
                "    {    \"model_class\":\"org.javalite.activejdbc.test_models.Address\",\n" +
                "      \"id\":\"1\",\n" +
                "      \"zip\":\"60606\",\n" +
                "      \"state\":\"IL\",\n" +
                "      \"address1\":\"123 Pine St.\",\n" +
                "      \"address2\":\"apt 31\",\n" +
                "      \"user_id\":\"1\",\n" +
                "      \"city\":\"Springfield\"\n" +
                "    },\n" +
                "    {    \"model_class\":\"org.javalite.activejdbc.test_models.Address\",\n" +
                "      \"id\":\"2\",\n" +
                "      \"zip\":\"60606\",\n" +
                "      \"state\":\"IL\",\n" +
                "      \"address1\":\"456 Brook St.\",\n" +
                "      \"address2\":\"apt 21\",\n" +
                "      \"user_id\":\"1\",\n" +
                "      \"city\":\"Springfield\"\n" +
                "    },\n" +
                "    {    \"model_class\":\"org.javalite.activejdbc.test_models.Address\",\n" +
                "      \"id\":\"3\",\n" +
                "      \"zip\":\"60606\",\n" +
                "      \"state\":\"IL\",\n" +
                "      \"address1\":\"23 Grove St.\",\n" +
                "      \"address2\":\"apt 32\",\n" +
                "      \"user_id\":\"1\",\n" +
                "      \"city\":\"Springfield\"\n" +
                "    }\n" +
                "]\n" +
                "}\n" +
                "}");
    }

    @Test
    public void shouldIncludeUglyChildren() throws IOException {
        deleteAndPopulateTables("users", "addresses");
        List<User> personList = User.findAll().orderBy("id").include(Address.class);
        User u = personList.get(0);
        String json = u.toJson(false);
        mapper.readTree(json);//check validity
        a(json).shouldEqual("{\"model_class\":\"org.javalite.activejdbc.test_models.User\",\"id\":\"1\",\"first_name\":\"Marilyn\",\"email\":\"mmonroe@yahoo.com\",\"last_name\":\"Monroe\",\"children\" : {\"addresses\" : [{\"model_class\":\"org.javalite.activejdbc.test_models.Address\",\"id\":\"1\",\"zip\":\"60606\",\"state\":\"IL\",\"address1\":\"123 Pine St.\",\"address2\":\"apt 31\",\"user_id\":\"1\",\"city\":\"Springfield\"},{\"model_class\":\"org.javalite.activejdbc.test_models.Address\",\"id\":\"2\",\"zip\":\"60606\",\"state\":\"IL\",\"address1\":\"456 Brook St.\",\"address2\":\"apt 21\",\"user_id\":\"1\",\"city\":\"Springfield\"},{\"model_class\":\"org.javalite.activejdbc.test_models.Address\",\"id\":\"3\",\"zip\":\"60606\",\"state\":\"IL\",\"address1\":\"23 Grove St.\",\"address2\":\"apt 32\",\"user_id\":\"1\",\"city\":\"Springfield\"}]}}");
    }

    @Test
    public void shouldIncludeOnlyProvidedAttributes() throws IOException {
        deleteAndPopulateTables("users", "addresses");

        User u = User.findById(1);
        String json = u.toJson(true, "email", "last_name");
        mapper.readTree(json);//check validity
        the(json).shouldBeEqual("{\n" +
                "  \"email\":\"mmonroe@yahoo.com\",\n" +
                "  \"last_name\":\"Monroe\"\n" +
                "}");
    }

    @Test
    public void shouldGenerateFromList() throws IOException {
        deleteAndPopulateTables("users", "addresses");
        LazyList<User> personList = User.findAll().orderBy("id").include(Address.class);

        String json = personList.toJson(false);
        mapper.readTree(json);//check validity
        a(json).shouldBeEqual("[{\"model_class\":\"org.javalite.activejdbc.test_models.User\",\"id\":\"1\",\"first_name\":\"Marilyn\",\"email\":\"mmonroe@yahoo.com\",\"last_name\":\"Monroe\",\"children\" : {\"addresses\" : [{\"model_class\":\"org.javalite.activejdbc.test_models.Address\",\"id\":\"1\",\"zip\":\"60606\",\"state\":\"IL\",\"address1\":\"123 Pine St.\",\"address2\":\"apt 31\",\"user_id\":\"1\",\"city\":\"Springfield\"},{\"model_class\":\"org.javalite.activejdbc.test_models.Address\",\"id\":\"2\",\"zip\":\"60606\",\"state\":\"IL\",\"address1\":\"456 Brook St.\",\"address2\":\"apt 21\",\"user_id\":\"1\",\"city\":\"Springfield\"},{\"model_class\":\"org.javalite.activejdbc.test_models.Address\",\"id\":\"3\",\"zip\":\"60606\",\"state\":\"IL\",\"address1\":\"23 Grove St.\",\"address2\":\"apt 32\",\"user_id\":\"1\",\"city\":\"Springfield\"}]}},{\"model_class\":\"org.javalite.activejdbc.test_models.User\",\"id\":\"2\",\"first_name\":\"John\",\"email\":\"jdoe@gmail.com\",\"last_name\":\"Doe\",\"children\" : {\"addresses\" : [{\"model_class\":\"org.javalite.activejdbc.test_models.Address\",\"id\":\"4\",\"zip\":\"60606\",\"state\":\"IL\",\"address1\":\"143 Madison St.\",\"address2\":\"apt 34\",\"user_id\":\"2\",\"city\":\"Springfield\"},{\"model_class\":\"org.javalite.activejdbc.test_models.Address\",\"id\":\"5\",\"zip\":\"60606\",\"state\":\"IL\",\"address1\":\"153 Creek St.\",\"address2\":\"apt 35\",\"user_id\":\"2\",\"city\":\"Springfield\"},{\"model_class\":\"org.javalite.activejdbc.test_models.Address\",\"id\":\"6\",\"zip\":\"60606\",\"state\":\"IL\",\"address1\":\"163 Gorge St.\",\"address2\":\"apt 36\",\"user_id\":\"2\",\"city\":\"Springfield\"},{\"model_class\":\"org.javalite.activejdbc.test_models.Address\",\"id\":\"7\",\"zip\":\"60606\",\"state\":\"IL\",\"address1\":\"173 Far Side.\",\"address2\":\"apt 37\",\"user_id\":\"2\",\"city\":\"Springfield\"}]}}]");
    }

    @Test
    public void shouldEscapeDoubleQuote() throws IOException {
        Page p = new Page();
        p.set("description", "bad \" description");
        JsonNode node = mapper.readTree(p.toJson(true));
        a(node.get("description").toString()).shouldBeEqual("\"bad \\\" description\"");

        //ensure no NPE:
        p = new Page();
        p.set("description", null);
        p.toJson(true);
    }


    @Test
    public void shouldInjectCustomContentIntoJson() throws IOException {
        deleteAndPopulateTable("posts");

        Post p = Post.findById(1);
        String json = p.toJson(true, "title");

        Map map = mapper.readValue(json, Map.class);
        Map injected = (Map) map.get("injected");
        a(injected.get("secret_name")).shouldBeEqual("Secret Name");
    }
}

