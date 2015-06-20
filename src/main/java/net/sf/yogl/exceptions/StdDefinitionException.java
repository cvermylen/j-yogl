/* Copyright (C) 2003 Symphonix
   
   This library is free software; you can redistribute it and/or
   modify it under the terms of the GNU Library General Public
   License as published by the Free Software Foundation; either
   version 2 of the License, or (at your option) any later version.
   
   This library is distributed in the hope that it will be useful,
   but WITHOUT ANY WARRANTY; without even the implied warranty of
   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
   Library General Public License for more details.
   
   You should have received a copy of the GNU Library General Public
   License along with this library; if not, write to the Free
   Software Foundation, Inc., 59 Temple Place - Suite 330, Boston,
   MA 02111-1307, USA */
   
package net.sf.yogl.exceptions;

/**
 * Describes an error detected during the validation of the STD. The STD
 * cannot be used
 */
public class StdDefinitionException extends GraphException {

	private static final long serialVersionUID = -2049423510053088502L;

	/**
	 * 
	 */
	public StdDefinitionException() {
		super();
	}

	/**
	 * @param arg0
	 * @param arg1
	 */
	public StdDefinitionException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	/**
	 * @param arg0
	 */
	public StdDefinitionException(Throwable arg0) {
		super(arg0);
	}

	/**
	 * @param s
	 */
	public StdDefinitionException(String s) {
		super(s);
	}

}
