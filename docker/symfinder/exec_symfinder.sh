#!/bin/sh
#
# This file is part of symfinder.
#
# symfinder is free software: you can redistribute it and/or modify
# it under the terms of the GNU Lesser General Public License as published by
# the Free Software Foundation, either version 3 of the License, or
# (at your option) any later version.
#
# symfinder is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
# GNU Lesser General Public License for more details.
#
# You should have received a copy of the GNU Lesser General Public License
# along with symfinder. If not, see <http://www.gnu.org/licenses/>.
#
# Copyright 2018-2019 Johann Mortara <johann.mortara@univ-cotedazur.fr>
# Copyright 2018-2019 Xhevahire Tërnava <xhevahire.ternava@lip6.fr>
# Copyright 2018-2019 Philippe Collet <philippe.collet@univ-cotedazur.fr>
#

set -e

if [[ -d /resources2 ]]; then
  echo "Copying resources to analyse in tmpfs mount..."
  cp -r /resources2/"$1" /resources/
  if [ -z "$4" ]; then
    java -jar /symfinder.jar /resources/ "$2" "$3"
  else
    java "$4" -jar /symfinder.jar /resources/ "$2" "$3"
  fi
else
  if [ -z "$4" ]; then
    java -jar /symfinder.jar /resources/"$1" "$2" "$3"
  else
    java "$4" -jar /symfinder.jar /resources/"$1" "$2" "$3"
  fi
fi

chown -R $SYMFINDER_UID:$SYMFINDER_GID /generated_visualizations
