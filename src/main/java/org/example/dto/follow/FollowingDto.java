package org.example.dto.follow;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.example.dto.member.MemberDto;

import java.util.List;

@Data
@RequiredArgsConstructor
public class FollowingDto {
    private List<MemberDto> followings;

    @Builder
    public FollowingDto(List<MemberDto> followings){
        this.followings=followings;
    }
}
