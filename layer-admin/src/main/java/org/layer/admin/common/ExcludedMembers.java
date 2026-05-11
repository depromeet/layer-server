package org.layer.admin.common;

import java.util.List;
import java.util.Set;

public final class ExcludedMembers {

    public static final Set<Long> IDS = Set.of(
        146L, 147L, 149L, 150L, 153L, 156L, 157L, 158L, 185L, 260L, 837L, 845L, 855L, 917L
    );

    public static final List<Long> ID_LIST = List.copyOf(IDS);

    private ExcludedMembers() {}
}
