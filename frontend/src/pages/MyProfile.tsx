import React, { useEffect, useState } from 'react';
import { fetchMyProfile, type UserProfile } from '../hooks/useProfile';

const MyProfile = () => {
  const [profile, setProfile] = useState<UserProfile | null>(null);

  useEffect(() => {
    fetchMyProfile().then(setProfile);
  }, []);

  if (!profile) return <div>Loading...</div>;

  return (
    <div>
      <h2>{profile.name}님의 프로필</h2>
      <div>아이디: {profile.username}</div>
      <div>이메일: {profile.email}</div>
      <div>역할: {profile.role}</div>
    </div>
  );
};

export default MyProfile;
