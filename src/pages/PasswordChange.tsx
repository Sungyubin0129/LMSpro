import React, { useState } from 'react';
import { changePassword } from '../hooks/useProfile';

const PasswordChange = () => {
  const [pw, setPw] = useState('');
  const [ok, setOk] = useState(false);

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      await changePassword(pw);
      setOk(true);
      setPw('');
    } catch {
      alert('비밀번호 변경 실패');
    }
  };

  return (
    <form onSubmit={handleSubmit}>
      <input
        type="password"
        value={pw}
        onChange={e => setPw(e.target.value)}
        placeholder="새 비밀번호"
      />
      <button type="submit">비밀번호 변경</button>
      {ok && <span>변경 완료!</span>}
    </form>
  );
};

export default PasswordChange;
