import React, { useEffect, useState, useContext } from 'react';
import api from '../api/axios';
import { AuthContext } from '../context/AuthContext';
import type { ChangeEvent } from 'react';
import type { EnrollmentResponse } from '../types';
// <내 수강 내역 불러오기>
// 2025/06/09 성유빈
// 사용자가 수강 중인 강의 목록과 진도율을 관리하는 페이지
export default function EnrollmentsPage() {
  const auth = useContext(AuthContext)!;
  const [list, setList] = useState<EnrollmentResponse[]>([]);
  const [error, setError] = useState<string | null>(null);

  // <내 수강 내역 불러오기>
  useEffect(() => {
    api.get<EnrollmentResponse[]>('/enrollments')
      .then(res => setList(res.data))
      .catch(err => {
        if (err.response?.status === 401) auth.logout();
        else setError('수강 내역을 불러오는 중 오류가 발생했습니다.');
      });
  }, [auth]);

  // input 값을 바꿀 때마다 state를 바로 업데이트해도 괜찮을지..?
  const onProgressChange = (id: number) => (e: ChangeEvent<HTMLInputElement>) => {
    const value = Number(e.target.value);
    setList(list.map(item => item.id === id ? { ...item, progress: value } : item));
  };

  // 서버에 진도율 저장: 이건 실패하면 어떻게 처리할지 고민해봐야겠다
  const updateProgress = async (id: number, progress: number) => {
    await api.patch<EnrollmentResponse>(`/enrollments/${id}/progress`, { progress });
    alert('진도율이 업데이트되었습니다.');
  };


  const complete = async (id: number) => {
    // 완료 처리 버튼 눌렀을 때, completedAt 포맷은 어떻게 보여줄지??
    await api.patch<EnrollmentResponse>(`/enrollments/${id}/complete`, {});
    setList(list.map(item => item.id === id ? { ...item, completed: true, completedAt: new Date().toISOString() } : item));
    alert('수강이 완료 처리되었습니다.');
  };

  return (
    <div className="max-w-3xl mx-auto mt-10">
      <h1 className="text-3xl mb-6">내 수강 내역</h1>
      {error && <p className="text-red-500 mb-4">{error}</p>}
      <table className="w-full table-auto border-collapse">
        <thead>
          <tr className="bg-gray-100">
            <th className="p-2 border">강의</th>
            <th className="p-2 border">진도(%)</th>
            <th className="p-2 border">완료 여부</th>
            <th className="p-2 border">액션</th>
          </tr>
        </thead>
        <tbody>
          {list.map(en => (
            <tr key={en.id}>
              <td className="p-2 border">{en.courseTitle}</td>
              <td className="p-2 border">
                <input
                  type="number"
                  min={0}
                  max={100}
                  value={en.progress}
                  onChange={onProgressChange(en.id)}
                  className="w-16 p-1 border rounded"
                />
              </td>
              <td className="p-2 border">
                {en.completed ? '✅ 완료' : '⏳ 진행 중'}
              </td>
              <td className="p-2 border space-x-2">
                <button
                  onClick={() => updateProgress(en.id, en.progress)}
                  className="px-2 py-1 bg-blue-500 text-white rounded"
                >
                  저장
                </button>
                {!en.completed && (
                  <button
                    onClick={() => complete(en.id)}
                    className="px-2 py-1 bg-green-500 text-white rounded"
                  >
                    완료 처리
                  </button>
                )}
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}
