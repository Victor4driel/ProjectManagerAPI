package com.vabp.project.service;

import com.vabp.project.model.Member;

import java.util.List;
import java.util.UUID;

public interface MemberService {
    Member createMember(Member member);

    List<Member> getMembers();

    Member getMemberById(UUID id);

    Member updateMember(UUID id, Member member);

    void deleteMember(UUID id);
}
