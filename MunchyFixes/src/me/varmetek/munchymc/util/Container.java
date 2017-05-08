package me.varmetek.munchymc.util;

import java.util.Objects;

/**
 * Created by XDMAN500 on 1/1/2017.
 */
public class Container<T>
{
	private T value;
	private static final Container<?> EMPTY = new Container<>();
	private Container() {
		this.value = null;
	}

	public static <T> Container<T> empty() {
		@SuppressWarnings("unchecked")
		Container<T> t = (Container<T>) EMPTY;
		return t;
	}

	private Container(T value) {
		this.value = Objects.requireNonNull(value);
	}
	public static <T> Container<T> of(T value) {
		return value == null ? empty() : new Container<>(value);
	}
	public boolean isPresent() {
		return value != null;
	}
	public T get() {

		return value;
	}
	public void set(T value) {

		this.value = value;
	}
}
