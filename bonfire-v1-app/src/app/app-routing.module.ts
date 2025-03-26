import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { MapViewComponent } from './features/map/components/map-view/map-view.component';
import { RegisterComponent } from './core/auth/components/register/register.component';
import { HomeComponent } from './core/components/home/home.component';
import { LoginComponent } from './core/auth/components/login/login.component';
import { authGuard } from './core/auth/guards/auth.guard';
import { ShowNodesComponent } from './features/collection/components/show-nodes/show-nodes.component';
import { ProfileHeaderComponent } from './features/profile/components/profile-header/profile-header.component';
import { ProfilePageComponent } from './features/profile/components/profile-page/profile-page.component';
import { ShowBonfiresComponent } from './features/collection/components/show-bonfires/show-bonfires.component';
import { SearchStuffComponent } from './features/search-results/components/search-stuff/search-stuff.component';
import { UserFeedComponent } from './features/feed/components/user-feed/user-feed.component';
import { SettingsComponent } from './features/profile/components/settings/settings.component';
import { FeedbackFormComponent } from './core/components/footer/feedback-form.component';

const routes: Routes = [
  {
    path: '',
    component: HomeComponent
  },
  {
    path: 'map',
    component: MapViewComponent,
    canActivate: [authGuard]
  },
  {
    path: 'register',
    component: RegisterComponent
  },
  {
    path: 'login',
    component: LoginComponent
  },
  {
    path: 'nodes',
    component: ShowNodesComponent,
    canActivate: [authGuard]
  },
  {
    path: 'bonfires',
    component: ShowBonfiresComponent,
    canActivate: [authGuard]
  },
  {
    path: 'profile',
    component: ProfilePageComponent,
    canActivate: [authGuard]
  },
  {
    path: 'profile/:username',
    component: ProfilePageComponent,
    canActivate: [authGuard]
  },
  {
    path: 'search',
    component: SearchStuffComponent,
    canActivate: [authGuard]
  },
  {
    path: 'feed',
    component: UserFeedComponent,
    canActivate: [authGuard]
  },
  {
    path: "settings",
    component: SettingsComponent,
    canActivate: [authGuard]
  },
  {
    path: "feedback",
    component: FeedbackFormComponent,
    canActivate: [authGuard]
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
