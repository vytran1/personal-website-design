import { Role } from '../role/role.model';

export interface User {
  id: number;
  email: string;
  provider: string;
  roles: Role[];
}
