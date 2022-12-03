package com.network.common.exceptions;
public class NetworkException extends Exception // Exception is Serializable
{
public NetworkException()
{

// do nothing
}

public NetworkException(String message)
{
super(message);
}


}