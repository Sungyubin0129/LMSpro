import React, { useContext } from 'react';           
import type { FC, ReactNode } from 'react';          
import { BrowserRouter, Routes, Route, Navigate }    
  from 'react-router-dom';

import { AuthProvider, AuthContext } from './context/AuthContext';
import LoginPage from './pages/LoginPage';
import CoursesPage from './pages/CoursesPage';
import EnrollmentsPage from './pages/EnrollmentsPage';
import MyProfile from './pages/MyProfile';
import PasswordChange from './pages/PasswordChange';

const PrivateRoute: FC<{ children: ReactNode }> = ({ children }) => {
  const auth = useContext(AuthContext);
  if (!auth || !auth.user) return <Navigate to="/login" />;
  return <>{children}</>;
};

const App: FC = () => (
  <AuthProvider>
    <BrowserRouter>
      <Routes>
        <Route path="/login" element={<LoginPage />} />
        <Route path="/profile" element={<MyProfile />} />
        <Route path="/profile/password" element={<PasswordChange />} />
        <Route
          path="/courses/*"
          element={
            <PrivateRoute>
              <CoursesPage />
            </PrivateRoute>
          }
        />
        <Route path="/enrollments" element={ <PrivateRoute><EnrollmentsPage /></PrivateRoute> }
        />
        <Route path="*" element={<Navigate to="/courses" replace />} />
      </Routes>
    </BrowserRouter>
  </AuthProvider>
);

export default App;
