import React, { useState, useContext } from 'react';
import { AuthContext } from '../context/AuthContext';
import type { AxiosError } from 'axios';
import type { FormEvent } from 'react';
import type { LoginRequest } from '../types';
// <로그인 페이지>
// 2025/06/11 성유빈
// 사용자가 로그인할 수 있는 페이지
const LoginPage: React.FC = () => {
  const auth = useContext(AuthContext)!;
  const [form, setForm] = useState<LoginRequest>({
    username: '',
    password: '',
  });
  const [error, setError] = useState<string | null>(null);

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) =>
    setForm({ ...form, [e.target.name]: e.target.value });

  const handleSubmit = async (e: FormEvent) => {
    e.preventDefault();
    try {
      await auth.login(form);
      window.location.href = '/courses';
    } catch (err) {
      if (err && typeof err === 'object' && 'response' in err) {
        const response = (err as AxiosError).response;
        setError('로그인 실패: ' + (response?.data?.message || '아이디/비밀번호를 확인하세요.'));
      } else {
        setError('로그인 실패: 아이디/비밀번호를 확인하세요.');
      }
    }
  };

  return (
    <div className="max-w-sm mx-auto mt-20">
      <h1 className="text-2xl mb-4">로그인</h1>
      {error && <p className="text-red-500 mb-2">{error}</p>}
      <form onSubmit={handleSubmit} className="space-y-4">
        <input
          name="username"
          type="text"
          placeholder="Username"
          value={form.username}
          onChange={handleChange}
          className="w-full p-2 border rounded"
        />
        <input
          name="password"
          type="password"
          placeholder="Password"
          value={form.password}
          onChange={handleChange}
          className="w-full p-2 border rounded"
        />
        <button
          type="submit"
          className="w-full p-2 bg-blue-600 text-white rounded"
        >
          로그인
        </button>
      </form>
      <p className="text-sm text-gray-500 mt-3">테스트 계정: admin / 1234</p>
      <a href="/register" className="text-blue-600 text-sm underline block mt-1">
        회원가입
      </a>
    </div>
  );
};

export default LoginPage;
