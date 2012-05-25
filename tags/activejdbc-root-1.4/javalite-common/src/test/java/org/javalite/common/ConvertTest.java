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

package org.javalite.common;

import org.javalite.test.jspec.JSpecSupport;
import org.junit.Test;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;


/**
 * @author Igor Polevoy
 */
public class ConvertTest extends JSpecSupport {

    @Test
    public void shouldCovertToSqlDate() throws ParseException {
        Date d = new Date(new SimpleDateFormat("yyyy-MM-dd").parse("2001-01-01").getTime());
        Date d1 = Convert.toSqlDate("2001-01-01");
        a(d).shouldBeEqual(d1);
    }


    @Test
    public void shouldTruncateToSqlDate() throws ParseException {


        //util date
        java.util.Date now = new java.util.Date();
        java.sql.Date today = Convert.truncateToSqlDate(now);
        String string = new java.util.Date(today.getTime()).toString();   //format: Fri Jun 17 12:55:47 CDT 2011
        a(string.contains("00:00:00")).shouldBeTrue();

        //util Timestamp
        Timestamp t = new Timestamp(System.currentTimeMillis());
        today = Convert.truncateToSqlDate(t);
        string = new java.util.Date(today.getTime()).toString();   //format: Fri Jun 17 12:55:47 CDT 2011
        a(string.contains("00:00:00")).shouldBeTrue();

        //util TIme
        Time tm = new Time(System.currentTimeMillis());
        today = Convert.truncateToSqlDate(tm);
        string = new java.util.Date(today.getTime()).toString();   //format: Fri Jun 17 12:55:47 CDT 2011
        a(string.contains("00:00:00")).shouldBeTrue();
    }


    @Test
    public void shouldCovertToTimestamp() throws ParseException {

        //string
        Timestamp ts = new Timestamp(System.currentTimeMillis());
        Timestamp ts1 = Convert.toTimestamp(ts.toString());
        a(ts).shouldBeEqual(ts1);

        //java.sql.Date
        java.sql.Date date = new Date(System.currentTimeMillis());
        ts1 = new Timestamp(date.getTime());
        ts = Convert.toTimestamp(date);
        a(ts).shouldBeEqual(ts1);

        //java.util.Date
        java.util.Date date1 = new java.util.Date(System.currentTimeMillis());
        ts1 = new Timestamp(date1.getTime());
        ts = Convert.toTimestamp(date1);
        a(ts).shouldBeEqual(ts1);
    }

    @Test
    public void shouldCovertToBigDecimal() throws ParseException {

        //integer
        Object o= Convert.toBigDecimal(1);
        a(o instanceof BigDecimal).shouldBeTrue();
        a(o).shouldBeEqual(1);

        //string
        o= Convert.toBigDecimal("1");
        a(o instanceof BigDecimal).shouldBeTrue();
        a(o).shouldBeEqual(1);

        //double
        o= Convert.toBigDecimal(1d);
        a(o instanceof BigDecimal).shouldBeTrue();
        a(o).shouldBeEqual(1);

        //long
        o= Convert.toBigDecimal(1L);
        a(o instanceof BigDecimal).shouldBeTrue();
        a(o).shouldBeEqual(1);
    }

    @Test
    public void shouldCovertToLong() throws ParseException {

        //integer
        Object o= Convert.toLong(1);
        a(o instanceof Long).shouldBeTrue();
        a(o).shouldBeEqual(1);

        //string
        o= Convert.toLong("1");
        a(o instanceof Long).shouldBeTrue();
        a(o).shouldBeEqual(1);

        //double
        o= Convert.toLong(1d);
        a(o instanceof Long).shouldBeTrue();
        a(o).shouldBeEqual(1);

        //BigDecimal
        o= Convert.toLong(new BigDecimal(1));
        a(o instanceof Long).shouldBeTrue();
        a(o).shouldBeEqual(1);
    }


    @Test
    public void shouldCovertToDouble() throws ParseException {

        //integer
        Object o= Convert.toDouble(1);
        a(o instanceof Double).shouldBeTrue();
        a(o).shouldBeEqual(1);

        //string
        o= Convert.toDouble("1");
        a(o instanceof Double).shouldBeTrue();
        a(o).shouldBeEqual(1);

        //long
        o= Convert.toDouble(1L);
        a(o instanceof Double).shouldBeTrue();
        a(o).shouldBeEqual(1);

        //BigDecimal
        o= Convert.toDouble(new BigDecimal(1));
        a(o instanceof Double).shouldBeTrue();
        a(o).shouldBeEqual(1);
    }

    @Test
    public void shouldCovertToInteger() throws ParseException {

        //float
        Object o= Convert.toFloat(1F);
        a(o instanceof Float).shouldBeTrue();
        a(o).shouldBeEqual(1);

        //string
        o= Convert.toFloat("1");
        a(o instanceof Float).shouldBeTrue();
        a(o).shouldBeEqual(1);

        //long
        o= Convert.toFloat(1L);
        a(o instanceof Float).shouldBeTrue();
        a(o).shouldBeEqual(1);

        //BigDecimal
        o= Convert.toFloat(new BigDecimal(1));
        a(o instanceof Float).shouldBeTrue();
        a(o).shouldBeEqual(1);
    }

    @Test
    public void shouldCovertToBoolean() throws ParseException {

        a(Convert.toBoolean("true")).shouldBeTrue();
        a(Convert.toBoolean("false")).shouldBeFalse();

        a(Convert.toBoolean("TRUE")).shouldBeTrue();
        a(Convert.toBoolean("FALSE")).shouldBeFalse();

        a(Convert.toBoolean("yes")).shouldBeTrue();
        a(Convert.toBoolean("no")).shouldBeFalse();

        a(Convert.toBoolean("y")).shouldBeTrue();
        a(Convert.toBoolean("n")).shouldBeFalse();

        a(Convert.toBoolean(1)).shouldBeTrue();
        a(Convert.toBoolean(0)).shouldBeFalse();
    }


}
