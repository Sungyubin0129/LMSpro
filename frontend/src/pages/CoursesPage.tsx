import React, { useEffect, useState, useContext } from 'react';
import api from '../api/axios';
import type { Course } from '../types';
import { AuthContext } from '../context/AuthContext';
// <강의 목록 페이지>
// 2025/06/02 성유빈
// 사용자가 수강 가능한 강의 목록을 보여주고, 수강 신청할 수 있는 페이지
const CoursesPage: React.FC = () => {
  const auth = useContext(AuthContext)!;
  const [courses, setCourses] = useState<Course[]>([]);

  useEffect(() => {
    api
      .get<Course[]>('/courses')
      .then(res => setCourses(res.data))
      .catch(err => {
        if (err.response?.status === 401) {
          auth.logout();
        }
      });
  }, [auth]);

  const handleEnroll = async (id: number) => {
    await api.post(`/courses/${id}/enroll`);
    alert('수강 신청 완료!');
  };

  return (
    <div className="max-w-3xl mx-auto mt-10">
      <h1 className="text-3xl mb-6">강의 목록</h1>
      <ul className="space-y-4">
        {courses.map(c => (
          <li
            key={c.id}
            className="p-4 border rounded flex justify-between items-center"
          >
            <div>
              <h2 className="text-xl">{c.title}</h2>
              <p className="text-gray-600">{c.description}</p>
            </div>
            <button
              onClick={() => handleEnroll(c.id)}
              className="px-4 py-2 bg-green-500 text-white rounded"
            >
              수강 신청
            </button>
          </li>
        ))}
      </ul>
    </div>
  );
};

export default CoursesPage;
