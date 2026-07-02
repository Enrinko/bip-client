package client.bip.cleintdontdeletefuck.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class GroupEntity {
    private Long id;
    private String groupName;

    public GroupEntity(String groupName) {
        this.groupName = groupName;
    }

    public GroupEntity(Long id, String groupName) {
        this.id = id;
        this.groupName = groupName;
    }
}
