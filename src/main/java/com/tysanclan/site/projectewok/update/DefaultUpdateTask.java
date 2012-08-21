/**
 * Tysan Clan Website
 * Copyright (C) 2008-2011 Jeroen Steenbeeke and Ties van de Ven
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.tysanclan.site.projectewok.update;

import java.util.Comparator;

/**
 * @author Jeroen Steenbeeke
 */
public class DefaultUpdateTask implements UpdateTask
{
	private String targetVersion;

	private static final Comparator<String> comparator = new VersionComparator();

	protected DefaultUpdateTask(String targetVersion)
	{
		this.targetVersion = targetVersion;
	}

	/**
	 * @see com.tysanclan.site.projectewok.update.UpdateTask#getTargetVersion()
	 */
	@Override
	public String getTargetVersion()
	{
		return targetVersion;
	}

	/**
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(UpdateTask o)
	{
		return comparator.compare(getTargetVersion(), o.getTargetVersion());
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((targetVersion == null) ? 0 : targetVersion.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DefaultUpdateTask other = (DefaultUpdateTask) obj;
		if (targetVersion == null)
		{
			if (other.targetVersion != null)
				return false;
		}
		else if (!targetVersion.equals(other.targetVersion))
			return false;
		return true;
	}

}
