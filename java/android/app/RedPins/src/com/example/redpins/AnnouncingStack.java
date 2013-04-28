package com.example.redpins;

import java.util.Stack;

import android.util.Log;

public class AnnouncingStack<T> extends Stack<T> {
	
	@Override
	public T push(T item) {
		Log.i("Push", "Pushed Fragment: " + item.toString());
		Log.i("Push", "Fragments in Stack: " + super.size());
		return super.push(item);
	}
	
	@Override
	public T pop() {
		T item = super.pop();
		Log.i("Pop", "Popped Fragment: " + item.toString());
		Log.i("Pop", "Fragments in Stack: " + super.size());
		return item;
	}

}
