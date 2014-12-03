package com.canigenus.common.constants;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;

public class GenericConstant {
public static final int HASH_ITERATION_NO=100;

public static final int MULTIPLE_CHOICE_SINGLE_ANSWER=1;
public static final int MULTIPLE_CHOICE_MULTIPLE_ANSWER=2;
public static final int TRUE_FALSE=3;
public static final int FILL_IN_THE_BLANK=4;
public static final int DRAG_AND_DROP=5;
public static final String SALT="salt";
public static final String PASSWORD="password";
public static final int MIN_PASSWORD_LENGTH=6;
public static final String ACTIVE="Y";
public static final String INACTIVE="N";
public static final String USERINFO="UserInfo";

public static final String CONTACT_MODULE_URL="CONTACT_MODULE_URL";


public int getMULTIPLE_CHOICE_SINGLE_ANSWER()
{
	return MULTIPLE_CHOICE_SINGLE_ANSWER;
}
public int getMULTIPLE_CHOICE_MULTIPLE_ANSWER()
{
	return MULTIPLE_CHOICE_MULTIPLE_ANSWER;
}
public int getTRUE_FALSE()
{
	return TRUE_FALSE;
}
public int getFILL_IN_THE_BLANK()
{
	return FILL_IN_THE_BLANK;
}
public int getDRAG_AND_DROP()
{
	return DRAG_AND_DROP;
}

public static final HttpClient HTTP_CLIENT = new HttpClient(
		new MultiThreadedHttpConnectionManager());


}
