/*******************************************************************************
 *     ___                  _   ____  ____
 *    / _ \ _   _  ___  ___| |_|  _ \| __ )
 *   | | | | | | |/ _ \/ __| __| | | |  _ \
 *   | |_| | |_| |  __/\__ \ |_| |_| | |_) |
 *    \__\_\\__,_|\___||___/\__|____/|____/
 *
 *  Copyright (c) 2014-2019 Appsicle
 *  Copyright (c) 2019-2023 QuestDB
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 ******************************************************************************/

package io.questdb.griffin.engine.functions.groupby;

import io.questdb.griffin.AbstractGriffinTest;
import org.junit.Test;

public class MinDoubleVecGroupByFunctionFactoryTest extends AbstractGriffinTest {

    @Test
    public void testAddColumn() throws Exception {
        // fix page frame size, because it affects AVG accuracy
        pageFrameMaxRows = 10_000;
        assertQuery(
                "avg\n" +
                        "0.511848387\n",
                "select round(avg(f),9) avg from tab",
                "create table tab as (select rnd_double(2) f from long_sequence(131))",
                null,
                "alter table tab add column b double",
                "avg\n" +
                        "0.511848387\n",
                false,
                true,
                true
        );

        assertQuery(
                "avg\tmin\n" +
                        "0.5008779999999999\t0.0032519916115479885\n",
                "select round(avg(f),6) avg, min(b) min from tab",
                "insert into tab select rnd_double(2), rnd_double(2) from long_sequence(469)",
                null,
                false,
                true,
                true
        );
    }

    @Test
    public void testAllNullThenOne() throws Exception {
        assertQuery(
                "min\n" +
                        "NaN\n",
                "select min(f) from tab",
                "create table tab as (select cast(null as double) f from long_sequence(33))",
                null,
                "insert into tab select 22.009 from long_sequence(1)",
                "min\n" +
                        "22.009\n",
                false,
                true,
                true
        );
    }

    @Test
    public void testSimple() throws Exception {
        assertQuery(
                "min\n" +
                        "0.0011075361080621349\n",
                "select min(f) from tab",
                "create table tab as (select rnd_double(2) f from long_sequence(131))",
                null,
                false,
                true,
                true
        );
    }
}