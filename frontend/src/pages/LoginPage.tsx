import React, { useState, useContext } from 'react';
import { AuthContext } from '../context/AuthContext';
import type { FormEvent } from 'react';
import type { LoginRequest } from '../types';
import { useNavigate } from 'react-router-dom';

const LoginPage: React.FC = () => {
  const auth = useContext(AuthContext)!;
  const [form, setForm] = useState<LoginRequest>({
    username: '',
    password: '',
  });
  const [error, setError] = useState<string | null>(null);

  const navigate = useNavigate();

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) =>
    setForm({ ...form, [e.target.name]: e.target.value });

  const handleSubmit = async (e: FormEvent) => {
    e.preventDefault();
    try {
      await auth.login(form);
      navigate('/myProfile');
      //window.location.href = '/courses';
    } catch (err) {
      if (err && typeof err === 'object' && 'response' in err) {
        const response = (err as any).response;
        setError('로그인 실패: ' + (response?.data?.message || '아이디/비밀번호를 확인하세요.'));
      } else {
        setError('로그인 실패: 아이디/비밀번호를 확인하세요.');
      }
    }
  };

  return (
    <div className="min-h-screen bg-gray-50 flex items-center justify-center">
      <div className="bg-white rounded-2xl shadow-xl p-8 w-full max-w-md">
        <h1 className="text-3xl font-bold mb-6 text-gray-800 tracking-tight text-center">로그인</h1>
        {error && (
          <p className="text-red-500 text-center mb-4">{error}</p>
        )}
        <form onSubmit={handleSubmit} className="space-y-5">
          <div>
            <input
              name="username"
              type="text"
              autoComplete="username"
              placeholder="아이디"
              value={form.username}
              onChange={handleChange}
              className="w-full p-3 border border-gray-200 rounded-xl focus:outline-none focus:ring-2 focus:ring-blue-400 transition"
            />
          </div>
          <div>
            <input
              name="password"
              type="password"
              autoComplete="current-password"
              placeholder="비밀번호"
              value={form.password}
              onChange={handleChange}
              className="w-full p-3 border border-gray-200 rounded-xl focus:outline-none focus:ring-2 focus:ring-blue-400 transition"
            />
          </div>
          <button
            type="submit"
            className="w-full p-3 bg-blue-600 hover:bg-blue-700 transition text-white font-semibold rounded-xl shadow-md"
          >
            로그인
          </button>
        </form>
        <div className="mt-6 text-center text-sm text-gray-400">
          아이디/비밀번호를 잊으셨나요?
        </div>
      </div>
    </div>
  );
};

export default LoginPage;
