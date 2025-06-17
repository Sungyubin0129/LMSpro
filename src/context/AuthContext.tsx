import React, {createContext,useState,useEffect,} from 'react';
import type { FC, ReactNode } from 'react';    
import api from '../api/axios';
import type { UserProfile, AuthResponse, LoginRequest } from '../types.ts';

interface AuthContextType {
  user: UserProfile | null;
  login: (data: LoginRequest) => Promise<void>;
  logout: () => void;
}

export const AuthContext = createContext<AuthContextType | undefined>(
  undefined
);

export const AuthProvider: FC<{ children: ReactNode }> = ({ children }) => {
  const [user, setUser] = useState<UserProfile | null>(null);
  // 에러 상태도 관리해야 할지 고민 중...
  // const [error, setError] = useState<string | null>(null);
  useEffect(() => {
    // 앱 로드 시 토큰이 있으면 사용자 정보 한 번 받아오기    
    const token = localStorage.getItem('token');
    
    // 토큰 없으면 요청 자체를 안 보내도록!
    if (!token) return;
    api
      .get<UserProfile>('/users/me')
        .then(res => setUser(res.data))
        // TODO: 프로필에 avatar나 role-based redirect 로직 추가?        
        .catch(() => {
          localStorage.removeItem('token');
          setUser(null);
      });    
  }, []);
  
  //로그인 함수: 토큰 저장 후 프로필 갱신
  const login = async (form: LoginRequest) => {
    const { data } = await api.post<AuthResponse>('/auth/login', form);
    localStorage.setItem('token', data.token);
    // 업데이트된 토큰으로 현재 사용자 정보 조회
    const profile = await api.get<UserProfile>('/users/me');
    setUser(profile.data);
  };
  
  // 로그아웃: 토큰 제거 + state 초기화
  const logout = () => {
    localStorage.removeItem('token');
    setUser(null);
     // 주의: redirect는 각각의 페이지에서 처리
  };

  return (
    <AuthContext.Provider value={{ user, login, logout }}>
      {children}
    </AuthContext.Provider>
  );
};
