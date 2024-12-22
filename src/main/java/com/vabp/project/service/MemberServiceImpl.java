package com.vabp.project.service;

import com.vabp.project.exception.MemberNotFound;
import com.vabp.project.model.Member;
import com.vabp.project.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;

    @Autowired
    public MemberServiceImpl(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public Member createMember(Member member) {
        return memberRepository.save(member);
    }

    @Override
    public List<Member> getMembers() {
        return memberRepository.findAll();
    }

    @Override
    public Member getMemberById(UUID id) {
        return memberRepository.findById(id).orElse(null);
    }

    @Override
    public Member updateMember(UUID id, Member member) {
        Member memberToUpdate = memberRepository.findById(id).orElse(null);

        if (memberToUpdate == null) {
            throw new MemberNotFound("Member not found");
        }

        if (member.getName() != null) {
            memberToUpdate.setName(member.getName());
        }

        if (member.getEmail() != null) {
            memberToUpdate.setEmail(member.getEmail());
        }

        if (member.getRole() != null) {
            memberToUpdate.setRole(member.getRole());
        }

        memberRepository.save(memberToUpdate);

        return memberToUpdate;
    }

    @Override
    public void deleteMember(UUID id) {
        memberRepository.deleteById(id);
    }
}
