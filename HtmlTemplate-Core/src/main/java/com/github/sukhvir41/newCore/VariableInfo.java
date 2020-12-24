/*
 * Copyright 2020 Sukhvir Thapar
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.sukhvir41.newCore;

import org.apache.commons.lang3.StringUtils;

import java.util.UUID;

public class VariableInfo {

    private String type;
    private final String name;
    private final String value;
    private String mappedName;

    public VariableInfo(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public void setType(String type) {
        if (this.type != null) {
            throw new IllegalArgumentException("Overwriting the type of variable. Name -> " + name);
        }

        this.type = type;
    }

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    public String getMappedName() {

        if (StringUtils.isBlank(this.mappedName)) {
            this.mappedName = this.getName() + "_" + this.getStrippedUUID();
        }

        return this.mappedName;
    }

    private String getStrippedUUID() {
        return UUID.randomUUID()
                .toString()
                .replace("-", "");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        VariableInfo that = (VariableInfo) o;

        if (!name.equals(that.name)) return false;
        return value.equals(that.value);
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + value.hashCode();
        return result;
    }
}
