package com.client.handlers;

public class ResponseHandlerSearch extends ResponseHandler
{

	@SuppressWarnings("unused")
	private String message;

	public ResponseHandlerSearch(String status, String message)
	{
		super("RES_SEARCH", status);
		this.message = message;
	}

	@Override
	public void run()
	{
		//parse message and show search results

	}

}
