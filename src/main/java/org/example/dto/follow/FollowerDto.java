package org.example.dto.follow;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.example.dto.member.MemberDto;
import org.example.dto.member.MemberFollow;


import java.util.List;

@Data
@RequiredArgsConstructor
public class FollowerDto {
    private List<MemberFollow> followers;
    @Builder
    public FollowerDto(List<MemberFollow> followers){
        this.followers=followers;
    }

}
