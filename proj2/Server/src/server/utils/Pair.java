package server.utils;

public class Pair<Left, Right>
{
	public Left first;
	public Right second;
	
	public Pair(Left first, Right second)
	{
		this.first = first;
		this.second = second;
	}
	
	@Override
	public boolean equals(Object o)
	{
		Pair<Left, Right> pair = (Pair<Left, Right>)o;
		return pair.first == this.first && pair.second == this.second;
	}
}
