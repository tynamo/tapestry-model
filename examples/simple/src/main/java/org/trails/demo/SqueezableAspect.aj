package org.trails.demo;

import javax.persistence.Entity;
import org.trails.io.Squeezable;

public aspect SqueezableAspect
{
	declare parents : (@Entity *) implements Squeezable;
}
