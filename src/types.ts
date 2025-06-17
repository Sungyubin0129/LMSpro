export interface UserProfile {
  id: number;
  username: string;
  name: string;
  email: string;
  role: 'ADMIN' | 'INSTRUCTOR' | 'STUDENT';
}

export interface AuthResponse {
  token: string;
  tokenType: string;
}

export interface LoginRequest {
  username: string;
  password: string;
}

export interface Course {
  id: number;
  title: string;
  description: string;
}

export interface EnrollmentResponse {
  id: number;
  courseId: number;
  courseTitle: string;
  progress: number;
  completed: boolean;
  completedAt: string | null;
}
