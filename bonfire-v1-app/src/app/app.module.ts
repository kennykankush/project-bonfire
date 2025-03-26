import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { BrowserAnimationsModule }  from '@angular/platform-browser/animations'
import { ReactiveFormsModule } from '@angular/forms';
import { NgSelectModule } from '@ng-select/ng-select';


import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { MapViewComponent } from './features/map/components/map-view/map-view.component';
import { LogNodeButtonComponent } from './features/map/components/log-node-button/log-node-button.component';
import { LongLatViewerComponent } from './features/map/components/long-lat-viewer/long-lat-viewer.component';
import { UnlockAnimationComponent } from './features/map/components/unlock-animation/unlock-animation.component';
import { FooterComponent } from './core/components/footer/footer.component';
import { LoginComponent } from './core/auth/components/login/login.component';
import { RegisterComponent } from './core/auth/components/register/register.component';
import { HomeComponent } from './core/components/home/home.component';
import { provideHttpClient, withInterceptors } from '@angular/common/http';
import { authInterceptor } from './core/auth/interceptors/auth.interceptor';
import { ShowNodesComponent } from './features/collection/components/show-nodes/show-nodes.component';
import { ProfileHeaderComponent } from './features/profile/components/profile-header/profile-header.component';
import { CountryFlagComponent } from './shared/components/country-flag/country-flag.component';
import { FollowButtonComponent } from './features/profile/components/follow-button/follow-button.component';
import { ProfilePageComponent } from './features/profile/components/profile-page/profile-page.component';
import { LayoutComponent } from './core/components/layout/layout.component';
import { CountryCircularFlagComponent } from './shared/components/country-circular-flag/country-circular-flag.component';
import { ShowBonfiresComponent } from './features/collection/components/show-bonfires/show-bonfires.component';
import { SearchStuffComponent } from './features/search-results/components/search-stuff/search-stuff.component';
import { UserFeedComponent } from './features/feed/components/user-feed/user-feed.component';
import { SettingsComponent } from './features/profile/components/settings/settings.component';
import { FeedbackFormComponent } from './core/components/footer/feedback-form.component';

@NgModule({
  declarations: [
    AppComponent,
    MapViewComponent,
    LogNodeButtonComponent,
    LongLatViewerComponent,
    UnlockAnimationComponent,
    FooterComponent,
    LoginComponent,
    RegisterComponent,
    HomeComponent,
    ShowNodesComponent,
    ProfileHeaderComponent,
    CountryFlagComponent,
    FollowButtonComponent,
    ProfilePageComponent,
    LayoutComponent,
    CountryCircularFlagComponent,
    ShowBonfiresComponent,
    SearchStuffComponent,
    UserFeedComponent,
    SettingsComponent,
    FeedbackFormComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    BrowserAnimationsModule,
    ReactiveFormsModule,
    NgSelectModule
  ],
  providers: [provideHttpClient(withInterceptors([authInterceptor]))],
  bootstrap: [AppComponent]
})
export class AppModule { }
