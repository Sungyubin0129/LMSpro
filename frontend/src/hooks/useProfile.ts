import api from '../api/axios';

// UserProfile 타입 (실제 백엔드 응답 구조에 맞게 수정)
export interface UserProfile {
  id: number;
  username: string;
  name: string;
  email: string;
  role: string;
}

export async function fetchMyProfile() {
  const res = await api.get<UserProfile>('/users/me');
  return res.data;
}

export async function changePassword(password: string) {
  await api.patch('/users/me/password', { password });
}
