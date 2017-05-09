package me.varmetek.core.scoreboard;

import java.util.Iterator;
import java.util.Optional;

/**
 * Created by XDMAN500 on 2/20/2017.
 */
 interface RenderOrder_ARCHIVE<T>
{


	void request (T b);

	void leave (T b);


	boolean  isEmpty ();

	Optional<T> getCurrent ();


	boolean hasRequested (T b);
	Iterator<T> iterator ();
}
