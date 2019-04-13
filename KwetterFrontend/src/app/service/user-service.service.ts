import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { User } from '../model/user';
import { Observable } from 'rxjs';
 
@Injectable()
export class UserService {
 
  private usersUrl: string;
  private headers: HttpHeaders;
 
  constructor(private http: HttpClient) {
    this.usersUrl = 'http://localhost:8080/kwetter-1.0/api/admin/get_all_users';
    let headers = new HttpHeaders();
    this.headers = headers.set('Access-Control-Allow-Headers', '*')
  }
 
  public findAll(): Observable<User[]> {
    return this.http.get<User[]>(this.usersUrl, {headers: this.headers})
  }
  
 
  public save(user: User) {
    return this.http.post<User>(this.usersUrl, user);
  }
}