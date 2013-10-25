/*
* Copyright 2006-2007 Javector Software LLC
*
* Licensed under the GNU General Public License, Version 2 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*      http://www.gnu.org/copyleft/gpl.html
*
* THE SOURCE CODE AND ACCOMPANYING FILES ARE PROVIDED WITHOUT ANY WARRANTY,
* WRITTEN OR IMPLIED.
*
* The copyright holder provides this software under other licenses for those
* wishing to include it with products or systems not licensed under the GPL.
* Contact licenses@javector.com for more information.
*/
package com.javector.ser.adaptive.util;

import com.javector.adaptive.jaxb.util.JAXBReflectionUtil;
import com.javector.adaptive.framework.util.BaseReflectionUtil;
import com.javector.soaj.SoajException;

import javax.xml.namespace.QName;

/**
 * Created by IntelliJ IDEA.
 * Auther: Rohit Agarwal
 * Date: Apr 30, 2006
 * Time: 3:17:45 PM
 * To change this template use File | Settings | File Templates.
 */
public class TestSplit {

    public static void main(String[] args){
        String data = "test/test1/test2";
        String data1 = "test\\test1\\test2";
        data1.indexOf("\\");
        String[] datas = data.split("/");
        String[] datas2 = data1.split("/");
        System.out.println("datas = " + datas[0] + datas[1]);
        System.out.println("datas2 = " + datas2[0]);
    }


}

class testQname {
    public static void main(String[] args) throws SoajException {
        JAXBReflectionUtil util = new JAXBReflectionUtil();
        QName name = new QName(BaseReflectionUtil.xsNS,"string");
        System.out.println("va;ue = " +util.isSimpleType(name));
    }
}
