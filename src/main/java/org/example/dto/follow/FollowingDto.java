package org.example.dto.follow;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.example.dto.member.MemberFollow;

import java.util.List;

@Data
@RequiredArgsConstructor
public class FollowingDto {
    private List<MemberFollow> followings;

    @Builder
    public FollowingDto(List<MemberFollow> followings){
        this.followings=followings;
    }
}
