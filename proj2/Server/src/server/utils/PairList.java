package server.utils;

import java.io.Serializable;
import java.util.HashSet;

public class PairList<Left, Right> implements Serializable
{
	private static final long serialVersionUID = -1155722055282213118L;
	private HashSet<Pair<Left, Right>> list;
	
	public PairList()
	{
		this.list = new HashSet<Pair<Left, Right>>();
	}
	
	public void pushPair(Left first, Right second)
	{
		Pair<Left, Right> pair = new Pair<Left, Right>(first, second);
		list.add(pair);
	}
	
	public void removePair(Left first, Right second)
	{
		Pair<Left, Right> pair = new Pair<Left, Right>(first, second);
		list.remove(pair);
	}
	
	public Right getSecondFromFirst(Left first)
	{
		for (Pair<Left, Right> pair : list)
		{
			if (pair.first == first)
				return pair.second;
		}
		return null;
	}
	
	public Left getFirstFromSecond(Right second)
	{
		for (Pair<Left, Right> pair : list)
		{
			if (pair.second == second)
				return pair.first;
		}
		return null;
	}

	public boolean firstExists(Left exists)
	{
		for (Pair<Left, Right> pair : list)
		{
			if (pair.first == exists)
				return true;
		}
		return false;
	}
}
