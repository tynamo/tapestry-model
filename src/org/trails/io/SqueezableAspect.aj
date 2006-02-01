package org.trails.io;

import javax.persistence.Entity;

public aspect SqueezableAspect
{
	declare parents : (@Entity *) implements Squeezable;
}
