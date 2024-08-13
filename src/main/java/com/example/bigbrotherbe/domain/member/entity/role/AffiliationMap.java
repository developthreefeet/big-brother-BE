package com.example.bigbrotherbe.domain.member.entity.role;

import java.util.HashMap;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class AffiliationMap {

    @Getter
    private final Map<Affiliation,Role> positions = new HashMap<>();
    private final String memberName;
    public enum Role{
    ROLE_USER, ROLE_ADMIN, ROLE_PRESIDENT, ROLE_MANAGER;
    }

    public enum Affiliation{
        YUNGSO("융소"),
        DICONDI("디콘디");
        private final String name;
        Affiliation(String name){
        this.name = name;
        }
        public String getName(){
            return this.name;
        }
        public static Affiliation fromName(String name) {
            for (Affiliation affiliation : values()) {
                if (affiliation.name.equalsIgnoreCase(name)) {
                    return affiliation;
                }
            }
            throw new IllegalArgumentException("그런 소속이 없습니다.");
        }
    }
    public void addPosition(String affiliation, String role){
        try {
            Affiliation affEnum = Affiliation.fromName(affiliation);
            Role roleEnum = Role.valueOf(role.toUpperCase());
            positions.put(affEnum, roleEnum);
        } catch (IllegalArgumentException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}
