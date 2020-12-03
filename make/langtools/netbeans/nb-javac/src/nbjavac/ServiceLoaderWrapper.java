/*
 * Copyright (c) 2020, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */
package nbjavac;

import com.sun.tools.javac.platform.PlatformProvider;
import java.util.ServiceLoader;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class ServiceLoaderWrapper<T> {

    private final ServiceLoader<T> loader;

    public ServiceLoaderWrapper(ServiceLoader<T> loader) {
        this.loader = loader;
    }

    public static <T> ServiceLoader<T> load(ModuleWrapper.ModuleLayer layer, Class<T> aClass) {
        return ServiceLoader.load(aClass); //XXX
    }

    public static <T> ServiceLoaderWrapper<T> load(Class<T> aClass) {
        return new ServiceLoaderWrapper<T>(ServiceLoader.load(aClass));
    }

    public static Iterable<PlatformProvider> load(Class<PlatformProvider> aClass, ClassLoader classLoader) {
        return ServiceLoader.load(aClass, classLoader);
    }

    public Stream<Provider<T>> stream() {
        return StreamSupport.stream(loader.spliterator(), false)
                             .map(v -> new Provider<T>() {
            @Override
            public Class<? extends T> type() {
                return (Class<T>) v.getClass();
            }

            @Override
            public T get() {
                return v;
            }
                             });
    }

    public interface Provider<T> {
        public Class<? extends T> type();
        public T get();
    }
    
}
