package com.vabp.project.service;

import com.vabp.project.exception.MemberNotFound;
import com.vabp.project.model.Member;
import com.vabp.project.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MemberServiceImplTest {

    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private MemberServiceImpl memberService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void createMember() {
        Member member = new Member("John Doe", "john.doe@example.com", "MANAGER", null, null);

        when(memberRepository.save(member)).thenReturn(member);

        Member createdMember = memberService.createMember(member);

        assertNotNull(createdMember);
        assertEquals("John Doe", createdMember.getName());
        verify(memberRepository, times(1)).save(member);
    }

    @Test
    void getMembers() {
        List<Member> members = Arrays.asList(
                new Member("Alice", "alice@example.com", "DEVELOPER", null, null),
                new Member("Bob", "bob@example.com", "TESTER", null, null)
        );

        when(memberRepository.findAll()).thenReturn(members);

        List<Member> returnedMembers = memberService.getMembers();

        assertEquals(2, returnedMembers.size());
        assertEquals("Alice", returnedMembers.get(0).getName());
        verify(memberRepository, times(1)).findAll();
    }

    @Test
    void getMemberById_Found() {
        UUID memberId = UUID.randomUUID();
        Member member = new Member("Alice", "alice@example.com", "DEVELOPER", null, null);
        member.setId(memberId);

        when(memberRepository.findById(memberId)).thenReturn(Optional.of(member));

        Member foundMember = memberService.getMemberById(memberId);

        assertNotNull(foundMember);
        assertEquals("Alice", foundMember.getName());
        verify(memberRepository, times(1)).findById(memberId);
    }

    @Test
    void getMemberById_NotFound() {
        UUID memberId = UUID.randomUUID();

        when(memberRepository.findById(memberId)).thenReturn(Optional.empty());

        Member foundMember = memberService.getMemberById(memberId);

        assertNull(foundMember);
        verify(memberRepository, times(1)).findById(memberId);
    }

    @Test
    void updateMember_Success() {
        UUID memberId = UUID.randomUUID();
        Member existingMember = new Member("Alice", "alice@example.com", "DEVELOPER", null, null);
        existingMember.setId(memberId);

        Member updatedMemberData = new Member("Alice Updated", "alice.updated@example.com", "MANAGER", null, null);

        when(memberRepository.findById(memberId)).thenReturn(Optional.of(existingMember));
        when(memberRepository.save(existingMember)).thenReturn(existingMember);

        Member updatedMember = memberService.updateMember(memberId, updatedMemberData);

        assertNotNull(updatedMember);
        assertEquals("Alice Updated", updatedMember.getName());
        assertEquals("alice.updated@example.com", updatedMember.getEmail());
        assertEquals("MANAGER", updatedMember.getRole());
        verify(memberRepository, times(1)).findById(memberId);
        verify(memberRepository, times(1)).save(existingMember);
    }

    @Test
    void updateMember_NotFound() {
        UUID memberId = UUID.randomUUID();
        Member updatedMemberData = new Member("Alice Updated", "alice.updated@example.com", "MANAGER", null, null);

        when(memberRepository.findById(memberId)).thenReturn(Optional.empty());

        assertThrows(MemberNotFound.class, () -> memberService.updateMember(memberId, updatedMemberData));
        verify(memberRepository, times(1)).findById(memberId);
        verify(memberRepository, never()).save(any());
    }

    @Test
    void deleteMember() {
        UUID memberId = UUID.randomUUID();

        doNothing().when(memberRepository).deleteById(memberId);

        memberService.deleteMember(memberId);

        verify(memberRepository, times(1)).deleteById(memberId);
    }
}
